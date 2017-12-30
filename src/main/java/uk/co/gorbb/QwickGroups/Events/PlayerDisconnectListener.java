package uk.co.gorbb.QwickGroups.Events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import uk.co.gorbb.QwickGroups.Group.Group;
import uk.co.gorbb.QwickGroups.Group.GroupManager;
import uk.co.gorbb.QwickGroups.Util.Messages;

public class PlayerDisconnectListener implements Listener {
	
	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerDisconnect(PlayerQuitEvent event) {
		Group group = GroupManager.getGroupManager().getGroup(event.getPlayer());
		
		if (group == null)
			return;
		
		if (group.isLeader(event.getPlayer())) {
			GroupManager.getGroupManager().removeGroup(group.getName());
			Messages.GROUP_DISBANDED.send(group, group.getName());
		}
		else
		{
			group.removePlayer(event.getPlayer());
			Messages.GROUP_PLAYERLEAVE.send(group, event.getPlayer().getName());
		}
		
	}
	
}