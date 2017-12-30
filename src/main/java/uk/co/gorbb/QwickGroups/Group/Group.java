package uk.co.gorbb.QwickGroups.Group;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
	
}