package uk.co.gorbb.QwickGroups;

import org.bukkit.configuration.Configuration;

public class Config {
	private Configuration config;
	
	private int 		maxGroupSize,
						radialDistance;
	
	private boolean 	radialMeasureY;
	
	private boolean		radialXpEnabled,
						radialProtectionEnabled,
						radialSpeedEnabled,
						gloablXpEnabled,
						globalPvpEnabled;
	
	private int			radialXpAmount,
						radialProtectionAmount,
						globalXpAmount,
						radialSpeedLevel;
	
	public int getMaxGroupSize() {
		return maxGroupSize;
	}

	public int getRadialDistance() {
		return radialDistance;
	}

	public boolean isRadialMeasureY() {
		return radialMeasureY;
	}

	public boolean isRadialXpEnabled() {
		return radialXpEnabled;
	}

	public boolean isRadialProtectionEnabled() {
		return radialProtectionEnabled;
	}

	public boolean isRadialSpeedEnabled() {
		return radialSpeedEnabled;
	}

	public boolean isGloablXpEnabled() {
		return gloablXpEnabled;
	}

	public boolean isGlobalPvpEnabled() {
		return globalPvpEnabled;
	}

	public float getRadialXpAmount() {
		return radialXpAmount / 100f;
	}

	public float getRadialProtectionAmount() {
		return radialProtectionAmount / 100f;
	}

	public float getGlobalXpAmount() {
		return globalXpAmount / 100f;
	}

	public int getRadialSpeedLevel() {
		return radialSpeedLevel;
	}

	Config(QwickGroups instance) {
		config = instance.getConfig();
		instance.saveDefaultConfig();
	}
	
	public void load() {
		maxGroupSize 			= config.getInt("maxGroupSize");
		radialDistance			= config.getInt("radial.distance");
		radialMeasureY 			= config.getBoolean("radial.measureY");
		
		radialXpEnabled			= config.getBoolean("radialBoosts.xp.enabled");
		radialProtectionEnabled	= config.getBoolean("radialBoosts.protected.enabled");
		radialSpeedEnabled		= config.getBoolean("radialBoosts.speed.enabled");
		gloablXpEnabled			= config.getBoolean("globalBoosts.xpShare.enabled");
		globalPvpEnabled		= config.getBoolean("globalBoosts.pvp.enabled");
		
		radialXpAmount			= config.getInt("radialBoosts.xp.amount");
		radialProtectionAmount	= config.getInt("radialBoosts.protection.amount");
		globalXpAmount			= config.getInt("globalBoosts.xpShare.amount");
		
		radialSpeedLevel		= config.getInt("radialBoosts.speed.level");
	}
}