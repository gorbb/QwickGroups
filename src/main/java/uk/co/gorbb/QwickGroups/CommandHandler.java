package uk.co.gorbb.QwickGroups;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import uk.co.gorbb.QwickGroups.Group.Group;
import uk.co.gorbb.QwickGroups.Group.GroupManager;
import uk.co.gorbb.QwickGroups.Util.Messages;

public class CommandHandler implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//Check sender is player (no console commands here)
		if (!(sender instanceof Player))
			return Messages.COMMAND_NOCONSOLE.send(sender);
		
		//Check arguments
		if (args.length == 0)
			onCommandHelp((Player)sender, command, label, args);
		else {
			switch (args[0].toLowerCase()) {
				case "help": 	onCommandHelp((Player)sender, command, label, args); 	break;
				case "invite":	onCommandInvite((Player)sender, command, label, args); 	break;
				case "accept":  onCommandAccept((Player)sender, command, label, args); 	break;
				case "decline": onCommandDecline((Player)sender, command, label, args); break;
				case "leave":	onCommandLeave((Player)sender, command, label, args);	break;
				case "disband": onCommandDisband((Player)sender, command, label, args); break;
				case "remove":	onCommandRemove((Player)sender, command, label, args);	break;
				default:		onCommandCreate((Player)sender, command, label, args); 	break;
			}
		}
		
		return true;
	}
	
	public void onCommandHelp(Player player, Command command, String label, String[] args) {
		Messages.COMMAND_HELP_TITLE.send(player);
		
		Messages.COMMAND_HELP_ITEM.send(player, label, "[name]", "Creates a group with the given name");
		Messages.COMMAND_HELP_ITEM.send(player, label, "invite [player]", "Invite a player to your group");
		Messages.COMMAND_HELP_ITEM.send(player, label, "accept", "Accept a group invite");
		Messages.COMMAND_HELP_ITEM.send(player, label, "decline", "Decline a group invite");
		Messages.COMMAND_HELP_ITEM.send(player, label, "remove [player]", "Remove a player from your group");
		Messages.COMMAND_HELP_ITEM.send(player, label, "leave", "Leave your current group");
		Messages.COMMAND_HELP_ITEM.send(player, label, "disband", "Disband your group");
	}
	
	public void onCommandCreate(Player player, Command command, String label, String[] args) {
		GroupManager groupManager = GroupManager.getGroupManager();
		
		//Check if player is in group
		if (groupManager.hasGroup(player)) {
			Messages.COMMAND_CREATE_ALREADYINGROUP.send(player, label);
			return;
		}
		
		//Check if group name exists
		if (groupManager.hasGroup(args[0])) {
			Messages.COMMAND_CREATE_EXISTS.send(player, args[0]);
			return;
		}
		
		//Create group
		Group group = groupManager.createGroup(args[0], player);
		
		Messages.COMMAND_CREATE_SUCCESS.send(player, group.getName());
	}
	
	public void onCommandInvite(Player player, Command command, String label, String[] args) {
		//Check arguments
		if (args.length < 2) {
			Messages.COMMAND_INVALID_ARGS.send(player, label, "invite [player]");
			return;
		}
		
		GroupManager groupManager = GroupManager.getGroupManager();
		Group group = groupManager.getGroup(player);
		
		//Check that player is in group
		if (group == null) {
			Messages.COMMAND_INVITE_NOTINGROUP.send(player);
			return;
		}
		
		//Check that player is leader of group
		if (!group.isLeader(player)) {
			Messages.COMMAND_INVITE_NOTLEADER.send(player);
			return;
		}
		
		//Search for other player
		Player otherPlayer = Bukkit.getPlayer(args[1]);
		if (otherPlayer == null) {
			Messages.COMMAND_PLAYERNOTFOUND.send(player, args[1]);
			return;
		}
		
		//Check if invited player is in group
		if (groupManager.hasGroup(otherPlayer)) {
			Messages.COMMAND_INVITE_INGROUP.send(player, otherPlayer.getName());
			return;
		}
		
		//Send invite
		groupManager.addInvite(group, otherPlayer);
		
		Messages.COMMAND_INVITE_SENT.send(player, otherPlayer.getName());
		
		Messages.INVITE_RECEIVED.send(otherPlayer, player.getName(), group.getName());
		Messages.INVITE_RECEIVED_COMMAND_ACCEPT.send(otherPlayer, args);
		Messages.INVITE_RECEIVED_COMMAND_DECLINE.send(otherPlayer);
	}
	
	public void onCommandAccept(Player player, Command command, String label, String[] args) {
		GroupManager groupManager = GroupManager.getGroupManager();

		//Check if player is in group
		if (groupManager.hasGroup(player)) {
			Messages.COMMAND_RESPOND_INGROUP.send(player, label);
			return;
		}
		
		//Check if player has active invite
		if (!groupManager.hasInvite(player)) {
			Messages.COMMAND_RESPOND_NOACTIVE.send(player);
			return;
		}
		
		//Make sure the group still exists
		if (!groupManager.getInviteGroupExists(player)) {
			Messages.COMMAND_RESPOND_NOEXIST.send(player);
			return;
		}
		
		//Accept invite
		Group group = groupManager.acceptInvite(player);
		
		Messages.COMMAND_RESPOND_ACCEPTED.send(player);
		Messages.GROUP_PLAYERJOIN.send(group, player.getName());
	}
	
	public void onCommandDecline(Player player, Command command, String label, String[] args) {
		GroupManager groupManager = GroupManager.getGroupManager();
		
		//Check if player has active invite
		if (!groupManager.hasInvite(player)) {
			Messages.COMMAND_RESPOND_NOACTIVE.send(player);
			return;
		}
		
		//Make sure the group still exists
		if (!groupManager.getInviteGroupExists(player)) {
			Messages.COMMAND_RESPOND_NOEXIST.send(player);
			return;
		}
		
		//Decline invite
		groupManager.declineInvite(player);
		
		Messages.COMMAND_RESPOND_DECLINED.send(player);
	}
	
	public void onCommandLeave(Player player, Command command, String label, String[] args) {
		GroupManager groupManager = GroupManager.getGroupManager();
		Group group = groupManager.getGroup(player);
		
		//Check player is in group
		if (group == null) {
			Messages.COMMAND_LEAVE_NOGROUP.send(player);
			return;
		}
		
		//Check player isn't leader of group
		if (group.isLeader(player)) {
			Messages.COMMAND_LEAVE_LEADER.send(player);
			return;
		}
		
		//Leave group
		group.removePlayer(player);
		
		Messages.COMMAND_LEAVE_SUCCESS.send(player, group.getName());
		Messages.GROUP_PLAYERLEAVE.send(group, player.getName());
	}

	public void onCommandDisband(Player player, Command command, String label, String[] args) {
		GroupManager groupManager = GroupManager.getGroupManager();
		Group group = groupManager.getGroup(player);

		//Check that player is in group
		if (group == null) {
			Messages.COMMAND_INVITE_NOTINGROUP.send(player);
			return;
		}
		
		//Check that player is leader of group
		if (!group.isLeader(player)) {
			Messages.COMMAND_INVITE_NOTLEADER.send(player);
			return;
		}
		
		//Disband group
		groupManager.removeGroup(group.getName());
		
		Messages.GROUP_DISBANDED.send(group, group.getName());
	}
	
	public void onCommandRemove(Player player, Command command, String label, String[] args) {
		//Check arguments
		if (args.length < 2) {
			Messages.COMMAND_INVALID_ARGS.send(player, label, "remove [player]");
			return;
		}
		
		GroupManager groupManager = GroupManager.getGroupManager();
		Group group = groupManager.getGroup(player);

		//Check that player is in group
		if (group == null) {
			Messages.COMMAND_INVITE_NOTINGROUP.send(player);
			return;
		}
		
		//Check that player is leader of group
		if (!group.isLeader(player)) {
			Messages.COMMAND_INVITE_NOTLEADER.send(player);
			return;
		}

		//Search for other player
		Player otherPlayer = Bukkit.getPlayer(args[1]);
		if (otherPlayer == null) {
			Messages.COMMAND_PLAYERNOTFOUND.send(player, args[1]);
			return;
		}
		
		//Remove player
		group.removePlayer(otherPlayer);
		
		Messages.COMMAND_REMOVE_SUCCESS.send(player, otherPlayer.getName());
		Messages.GROUP_PLAYERREMOVE.send(group, otherPlayer.getName());
		Messages.PLAYER_REMOVED.send(otherPlayer, group.getName());
	}
	
	/*
	 * 
	 * group
	 * group help
	 * 
	 * group [name]
	 * 
	 * group invite [player]
	 * 
	 * group accept
	 * group decline
	 * 
	 * group leave
	 * 
	 * group remove [player]**
	 * 
	 * group disband [player]**
	 * 
	 */
}