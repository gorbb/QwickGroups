package uk.co.gorbb.QwickGroups.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

import uk.co.gorbb.QwickGroups.QwickGroups;
import uk.co.gorbb.QwickGroups.Group.Group;
import uk.co.gorbb.QwickGroups.Group.GroupManager;

public class XpShareListener implements Listener {
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerXpGain(PlayerExpChangeEvent event) {
		//First check if the player is in a group
		Group group = GroupManager.getGroupManager().getGroup(event.getPlayer());
		
		if (group == null)
			return;
		
		//Now work out how much xp to apply
		int amount = event.getAmount();
		
		if (amount <= 0)
			return;
		
		double multiplier = QwickGroups.get().getLocalConfig().getGlobalXpAmount();
		
		//Round up to the nearest whole number
		int extra = (int) Math.ceil(multiplier * amount);
		
		//Apply the XP, excluding the current player
		group.applyExp(extra, event.getPlayer());
		
		QwickGroups.get().getLogger().info(event.getPlayer().getName() + " gained " + amount + " xp, giving " + extra + " to the other " + group.countMembers() + " member(s)");
	}
}