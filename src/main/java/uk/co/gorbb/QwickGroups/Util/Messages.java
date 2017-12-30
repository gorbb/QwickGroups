package uk.co.gorbb.QwickGroups.Util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import uk.co.gorbb.QwickGroups.Group.Group;

public enum Messages {
	COMMAND_NOCONSOLE("&4This command cannot be run in console"),
	COMMAND_PLAYERNOTFOUND("&cPlayer &7{0}&c not found"),
	COMMAND_INVALID_ARGS("&cInvalid arguments. Try &7/{0} {1}"),
	
	COMMAND_HELP_TITLE("&6 === QwickGroups Commands ==="),
	COMMAND_HELP_ITEM("&c/{0} {1} &8- &f{2}"),
	
	COMMAND_CREATE_ALREADYINGROUP("&cYou are already in a group. Leave or disband the group first"),
	COMMAND_CREATE_EXISTS("&cA group named &7{0}&c already exists"),
	COMMAND_CREATE_SUCCESS("&2Created group &7{0}&c"),
	
	COMMAND_INVITE_NOTINGROUP("&cYou are not in a group"),
	COMMAND_INVITE_NOTLEADER("&cYou are not the leader of this group"),
	COMMAND_INVITE_INGROUP("&7{0}&c is already in a group"),
	COMMAND_INVITE_SENT("&2Sent an invite to &7{0}"),
	
	COMMAND_RESPOND_INGROUP("&cYou are already in a group. Leave or disband the group first"),
	COMMAND_RESPOND_NOACTIVE("&cYou do not have an active invite"),
	COMMAND_RESPOND_NOEXIST("&cThe group no longer exists"),
	COMMAND_RESPOND_ACCEPTED("&2Invite accepted"),
	COMMAND_RESPOND_DECLINED("&cInvite declined"),
	
	COMMAND_LEAVE_NOGROUP("&cYou are not in a group"),
	COMMAND_LEAVE_LEADER("&cGroup leaders cannot leave groups"),
	COMMAND_LEAVE_SUCCESS("&2You have left &7{0}"),
	
	COMMAND_REMOVE_SUCCESS("&2Removed {0} from group"),
	
	INVITE_RECEIVED("&b{0} wants you to join {1}"),
	INVITE_RECEIVED_COMMAND_ACCEPT("&c/group accept &7to join this group"),
	INVITE_RECEIVED_COMMAND_DECLINE("&c/group decline &7to decline this request"),
	
	GROUP_PLAYERJOIN("&b{0} has joined your group"),
	GROUP_PLAYERLEAVE("&b{0} has left your group"),
	GROUP_PLAYERREMOVE("&b{0} has been removed from your group"),
	GROUP_DISBANDED("&b{0} has been disbanded"),
	
	PLAYER_REMOVED("&bYou have been removed from {0}"),
	
	CUSTOM("{0}"),
	;
	
	private String text;
	
	private Messages(String text) {
		this.text = text;
	}
	
	private String prepareMessage(String... args) {
		String text = ChatColor.translateAlternateColorCodes('&', this.text);
		
		for (int index = 0; index < args.length; index++)
			text = text.replaceAll("\\{" + index + "}", args[index]);
		
		return text;
	}
	
	public boolean send(CommandSender sender, String... args) {
		sender.sendMessage(prepareMessage(args));
		
		return true;
	}
	
	public boolean send(Group group, String... args) {
		group.sendMessage(prepareMessage(args));
		
		return true;
	}
}