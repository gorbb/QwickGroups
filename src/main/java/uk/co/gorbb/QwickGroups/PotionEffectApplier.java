package uk.co.gorbb.QwickGroups;

import java.util.Iterator;

import uk.co.gorbb.QwickGroups.Group.Group;
import uk.co.gorbb.QwickGroups.Group.GroupManager;

public class PotionEffectApplier implements Runnable {
	
	private Config config;
	
	public PotionEffectApplier() {
		config = QwickGroups.get().getLocalConfig();
	}
	
	@Override
	public void run() {
		Iterator<Group> iterator = GroupManager.getGroupManager().getGroupIterator();
		
		while (iterator.hasNext())
			iterator.next().applyEffectsToPlayers(config.getBoostRadiusEffects(), config.getRadialDistance(), config.isRadialMeasureY());
	}
	
}