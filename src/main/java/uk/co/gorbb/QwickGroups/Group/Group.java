package uk.co.gorbb.QwickGroups.Group;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Group {
	private String name;
	private UUID leader;
	
	private List<UUID> members;
	
	Group(String name, UUID leader) {
		this.name 	= name;
		this.leader = leader;
		members 	= new LinkedList<UUID>();
	}
	
	public String getName() {
		return name;
	}
	
	public UUID getLeader() {
		return leader;
	}
	
	public int countMembers() {
		return members.size();
	}
	
	public boolean hasPlayer(Player player) {
		if (isLeader(player))
			return true;
		
		return members.contains(player.getUniqueId());
	}
	
	public boolean isLeader(Player player) {
		return leader.equals(player.getUniqueId());
	}
	
	public boolean addPlayer(Player player) {
		if (hasPlayer(player))
			return false;
		
		return members.add(player.getUniqueId());
	}
	
	public boolean removePlayer(Player player) {
		if (!hasPlayer(player))
			return false;
		
		return members.remove(player.getUniqueId());
	}
	
	public void sendMessage(String message) {
		Bukkit.getPlayer(leader).sendMessage(message);
		Iterator<UUID> iterator = members.iterator();

		while (iterator.hasNext())
			Bukkit.getPlayer(iterator.next()).sendMessage(message);
	}
	
	
	public void applyExp(int exp, Player exclude) {
		UUID excludeUUID = exclude.getUniqueId();
		Iterator<UUID> iterator = members.iterator();
		
		if (!excludeUUID.equals(leader))
			Bukkit.getPlayer(leader).giveExp(exp);
		

		while (iterator.hasNext())
			Bukkit.getPlayer(iterator.next()).giveExp(exp);
	}
	
	public boolean hasGroupInRange(Player player, int distance, boolean measureY) {
		//Sqaure the distance to save a bit of pc brain power
		distance *= distance;
		
		if (inRange(Bukkit.getPlayer(leader), player, distance, measureY))
			return true;
		
		Iterator<UUID> iterator = members.iterator();
		
		while (iterator.hasNext())
			if (inRange(Bukkit.getPlayer(iterator.next()), player, distance, measureY))
				return true;
		
		return false;
	}
	
	private boolean inRange(Player thisPlayer, Player otherPlayer, int distance, boolean measureY) {
		//Check that they aren't the same player
		if (thisPlayer.getUniqueId().equals(otherPlayer.getUniqueId()))
			return false;
		
		//Check the players are in the same world
		if (!thisPlayer.getWorld().equals(otherPlayer.getWorld()))
			return false;
		
		double sqrDist = 0;
		
		if (measureY)
			sqrDist = thisPlayer.getLocation().distanceSquared(otherPlayer.getLocation());
		else {
			//Manually check the distance
			double x1 = thisPlayer.getLocation().getX();
			double x2 = otherPlayer.getLocation().getX();
			double z1 = thisPlayer.getLocation().getZ();
			double z2 = otherPlayer.getLocation().getZ();
			
			double dX = x2 - x1;
			double dZ = z2 - z1;
			
			sqrDist = dX * dX + dZ * dZ;
		}
		
		return sqrDist <= distance;
	}
	
	private List<Player> getAllPlayersInRange(int distance, boolean measureY) {
		distance *= distance;
		
		List<Player> playersInRange = new LinkedList<Player>();
		Stack<Player> playersToCheck = new Stack<Player>();
		
		//Add members to the check list
		playersToCheck.add(Bukkit.getPlayer(leader));
		Iterator<UUID> iterator = members.iterator();
		
		while (iterator.hasNext())
			playersToCheck.add(Bukkit.getPlayer(iterator.next()));
		
		//Now start checking
		while (!playersToCheck.isEmpty()) {
			Player current = playersToCheck.pop();
			
			iterator = members.iterator();
			
			while (iterator.hasNext()) {
				Player otherPlayer = Bukkit.getPlayer(iterator.next());
				
				if (inRange(current, otherPlayer, distance, measureY)) {
					if (!playersInRange.contains(current))
						playersInRange.add(current);
					
					if (!playersInRange.contains(otherPlayer))
						playersInRange.add(otherPlayer);
					
					playersToCheck.remove(otherPlayer);
				}
			}
		}
		
		return playersInRange;
	}
	
	public void applyEffectsToPlayers(List<PotionEffect> effects, int distance, boolean measureY) {
		List<Player> players 		= getAllPlayersInRange(distance, measureY);
		Iterator<Player> iterator 	= players.iterator();
		
		while (iterator.hasNext()) 
			applyEffectsToPlayer(iterator.next(), effects);
	}
	
	private void applyEffectsToPlayer(Player player, List<PotionEffect> effects) {
		for (PotionEffect effect : effects)
			player.addPotionEffect(effect, true);
	}
	
}