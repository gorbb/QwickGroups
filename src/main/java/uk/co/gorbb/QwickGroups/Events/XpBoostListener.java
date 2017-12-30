package uk.co.gorbb.QwickGroups.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import uk.co.gorbb.QwickGroups.Config;
import uk.co.gorbb.QwickGroups.QwickGroups;
import uk.co.gorbb.QwickGroups.Group.Group;
import uk.co.gorbb.QwickGroups.Group.GroupManager;

public class XpBoostListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerXpGain(PlayerExpChangeEvent event) {
		//First check if the player is in a group
		Player player = event.getPlayer();
		
		Group group = GroupManager.getGroupManager().getGroup(player);
		
		if (group == null)
			return;
		
		//Now check to see if there's any other players in range
		Config config = QwickGroups.get().getLocalConfig();
		
		int radius 		 = config.getRadialDistance();
		double boost 	 = 1 + config.getRadialXpAmount();
		boolean measureY = config.isRadialMeasureY();
		
		if (!group.hasGroupInRange(player, radius, measureY))
			return;
		
		QwickGroups.get().getLogger().info("Players in range, adding xp boost");
		
		//Add the boost
		int amount = event.getAmount();
		amount = (int)Math.ceil(amount * boost);
		
		event.setAmount(amount);
	}
}