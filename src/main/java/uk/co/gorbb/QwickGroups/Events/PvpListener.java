package uk.co.gorbb.QwickGroups.Events;

import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import uk.co.gorbb.QwickGroups.Group.Group;
import uk.co.gorbb.QwickGroups.Group.GroupManager;

public class PvpListener implements Listener {
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerHurt(EntityDamageByEntityEvent event) {
		//Check that the damaged entity is a player
		if (!(event.getEntity() instanceof Player))
			return;
		
		Player player = (Player)event.getEntity();
		Group group = GroupManager.getGroupManager().getGroup(player);
		
		//Check that the damaged player is in a group
		if (group == null)
			return;
		
		//Check if the damager is a player and is also in the group
		if (event.getDamager() instanceof Player) {
			Player damager = (Player)event.getDamager();
			
			if (group.hasPlayer(damager))
				event.setCancelled(true);
		}
		//Check if the damager is a throwable, thrown by a player in the group
		else if (event.getDamager() instanceof Projectile) {
			Projectile projectile = (Projectile)event.getDamager();
			
			if (!(projectile.getShooter() instanceof Player))
				return;
			
			Player shooter = (Player)projectile.getShooter();
			
			if (group.hasPlayer(shooter))
				event.setCancelled(true);
		}
	}
	
}