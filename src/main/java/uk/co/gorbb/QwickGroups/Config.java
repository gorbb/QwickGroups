package uk.co.gorbb.QwickGroups;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Config {
	private Configuration config;
	
	private int 		maxGroupSize,
						radialDistance;
	
	private boolean 	radialMeasureY;
	
	private boolean 	boostXpShareEnabled;
	private double 		boostXpShareAmount;
	
	private boolean 	boostPvpEnabled;
	
	private boolean		boostXpRadiusEnabled;
	private double		boostXpRadiusAmount;
	
	private List<PotionEffect> boostRadiusEffects;
	
	public int getMaxGroupSize() {
		return maxGroupSize;
	}

	public int getRadialDistance() {
		return radialDistance;
	}

	public boolean isRadialMeasureY() {
		return radialMeasureY;
	}

	public boolean isBoostXpShareEnabled() {
		return boostXpShareEnabled;
	}

	public double getBoostXpShareAmount() {
		return boostXpShareAmount;
	}

	public boolean isBoostPvpEnabled() {
		return boostPvpEnabled;
	}

	public boolean isBoostXpRadiusEnabled() {
		return boostXpRadiusEnabled;
	}

	public double getBoostXpRadiusAmount() {
		return boostXpRadiusAmount;
	}

	public List<PotionEffect> getBoostRadiusEffects() {
		return boostRadiusEffects;
	}

	Config(QwickGroups instance) {
		config = instance.getConfig();
		instance.saveDefaultConfig();
		
		boostRadiusEffects = new LinkedList<PotionEffect>();
	}
	
	public void load() {
		maxGroupSize 			= config.getInt("maxGroupSize");
		radialDistance			= config.getInt("radial.distance");
		radialMeasureY 			= config.getBoolean("radial.measureY");

		boostXpShareEnabled		= config.getBoolean("boosts.xpShare.enabled");
		boostXpShareAmount		= config.getDouble("boosts.xpShare.amount");
		
		boostPvpEnabled			= config.getBoolean("boosts.pvp.enabled");
		
		boostXpRadiusEnabled	= config.getBoolean("boosts.xpRadius.enabled");
		boostXpRadiusAmount		= config.getDouble("boosts.xpRadius.amount");
		
		List<String> effects 	= config.getStringList("boosts.effects");
		
		//Process effects
		Iterator<String> iterator = effects.iterator();
		
		while (iterator.hasNext()) {
			String[] effect = iterator.next().split(":");
			
			PotionEffectType type = PotionEffectType.getByName(effect[0]);
			Integer level = 0;
			
			if (effect.length > 1)
				level = tryParse(effect[1], 0);
			
			boostRadiusEffects.add(new PotionEffect(type, 100, level, true, false));
		}
	}
	
	private int tryParse(String string, int def) {
		try {
			return Integer.parseInt(string);
		}
		catch (NumberFormatException exception) {
			return def;
		}
	}
}