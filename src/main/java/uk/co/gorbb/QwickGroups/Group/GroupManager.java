package uk.co.gorbb.QwickGroups.Group;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

public class GroupManager {
	
	private static GroupManager instance;
	
	public static GroupManager getGroupManager() {
		if (instance == null)
			instance = new GroupManager();
		
		return instance;
	}
	
	private HashMap<UUID, String> invites;
	private List<Group> groups;
	
	private GroupManager() {
		invites = new HashMap<UUID, String>();
		groups 	= new LinkedList<Group>();
	}
	
	public Iterator<Group> getGroupIterator() {
		return groups.iterator();
	}
	
	public Group createGroup(String name, Player leader) {
		if (hasGroup(name) || hasGroup(leader))
			return null;
		
		Group group = new Group(name, leader.getUniqueId());
		
		groups.add(group);
		return group;
	}
	
	public boolean hasGroup(String name) {
		Iterator<Group> iterator = groups.iterator();
		
		while (iterator.hasNext()) {
			Group current = iterator.next();
			
			if (current.getName().equalsIgnoreCase(name))
				return true;
		}
		
		return false;
	}
	
	public boolean hasGroup(Player player) {
		Iterator<Group> iterator = groups.iterator();

		while (iterator.hasNext()) {
			Group current = iterator.next();
			
			if (current.hasPlayer(player))
				return true;
		}
		
		return false;
	}
	
	public boolean isGroupLeader(Player player) {
		Iterator<Group> iterator = groups.iterator();

		while (iterator.hasNext()) {
			Group current = iterator.next();
			
			if (current.isLeader(player))
				return true;
		}
		
		return false;
	}
	
	public Group getGroup(String name) {
		Iterator<Group> iterator = groups.iterator();

		while (iterator.hasNext()) {
			Group current = iterator.next();
			
			if (current.getName().equalsIgnoreCase(name))
				return current;
		}
		
		return null;
	}
	
	public Group getGroup(Player player) {
		Iterator<Group> iterator = groups.iterator();

		while (iterator.hasNext()) {
			Group current = iterator.next();
			
			if (current.hasPlayer(player))
				return current;
		}
		
		return null;
	}
	
	public Group getGroupByLeader(Player player) {
		Iterator<Group> iterator = groups.iterator();

		while (iterator.hasNext()) {
			Group current = iterator.next();
			
			if (current.isLeader(player))
				return current;
		}
		
		return null;
	}
	
	public boolean removeGroup(String name) {
		Group group = getGroup(name);
		
		if (group == null)
			return false;
		
		return groups.remove(group);
	}
	
	public boolean removeGroupByLeader(Player leader) {
		Group group = getGroupByLeader(leader);
		
		if (group == null)
			return false;
		
		return groups.remove(group);
	}
	
	public boolean addInvite(Group group, Player player) {
		if (group == null || player == null)
			return false;
		
		if (!groups.contains(group) || hasGroup(player))
			return false;
		
		invites.put(player.getUniqueId(), group.getName());
		return true;
	}
	
	public boolean getInviteGroupExists(Player player) {
		if (!hasInvite(player))
			return false;
		
		return hasGroup(invites.get(player.getUniqueId()));
	}
	
	public boolean hasInvite(Player player) {
		return invites.containsKey(player.getUniqueId());
	}
	
	public Group acceptInvite(Player player) {
		if (!hasInvite(player))
			return null;
		
		String name = invites.remove(player.getUniqueId());
		Group group = getGroup(name);
		
		group.addPlayer(player);
		
		return group;
	}
	
	public boolean declineInvite(Player player) {
		if (!hasInvite(player))
			return false;
		
		invites.remove(player.getUniqueId());
		
		return true;
	}
}