package uk.co.gorbb.QwickGroups;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import uk.co.gorbb.QwickGroups.Events.PlayerDisconnectListener;
import uk.co.gorbb.QwickGroups.Events.PvpListener;
import uk.co.gorbb.QwickGroups.Events.XpShareListener;
import uk.co.gorbb.QwickGroups.Group.GroupManager;

public class QwickGroups extends JavaPlugin {
	private static QwickGroups plugin;
	
	public static QwickGroups get() { return plugin; }
	
	private Config config;
	
	public Config getLocalConfig() { return config; }
	
	@Override
	public void onEnable() {
		plugin = this;
		
		//Start group manager
		GroupManager.getGroupManager();
		
		//Load config
		config = new Config(this);
		config.load();
		
		//Set command executor
		getCommand("group").setExecutor(new CommandHandler());
		
		//Register listeners
		PluginManager pluginManager = getServer().getPluginManager();
		
		pluginManager.registerEvents(new PlayerDisconnectListener(), this);
		
		//Check config for other listeners
		loadBoostListeners(pluginManager);
	}
	
	@Override
	public void onDisable() {
		getLogger().info("Disable");
	}
	
	private void loadBoostListeners(PluginManager pluginManager) {
		//XP Share (global)
		if (config.isGloablXpEnabled())
			pluginManager.registerEvents(new XpShareListener(), this);
		
		//PvP (global)
		if (config.isGlobalPvpEnabled())
			pluginManager.registerEvents(new PvpListener(), this);
	}
	
}