package com.github.maxopoly.angeliacore.plugin;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PluginManager {

	private ServerConnection connection;
	private PluginService pluginService;
	private Map<String, AngeliaPlugin> plugins;
	private List<AngeliaPlugin> runningPlugins;

	public PluginManager(ServerConnection connection) {
		this.plugins = new HashMap<String, AngeliaPlugin>();
		this.runningPlugins = new LinkedList<AngeliaPlugin>();
		this.pluginService = PluginService.getInstance(connection.getLogger());
		this.connection = connection;
		reloadPlugins();
	}

	private AngeliaPlugin getPlugin(String name) {
		return plugins.get(name);
	}

	public void reloadPlugins() {
		pluginService.reloadJars();
		for (AngeliaPlugin plugin : pluginService.getAvailablePlugins()) {
			registerPlugin(plugin);
		}
		connection.getLogger().info("Loaded a total of " + plugins.size() + " plugin(s)");
	}

	private void registerPlugin(AngeliaPlugin plugin) {
		if (plugins.containsKey(plugin.getName().toLowerCase())) {
			connection.getLogger().warn("Plugin " + plugin.getName() + " was already registered, did not register again");
			return;
		}
		connection.getLogger().info("Registering plugin " + plugin.getName());
		plugins.put(plugin.getName().toLowerCase(), plugin);
	}

	public synchronized List<String> getAvailablePlugins() {
		// map values are all lower case, so we use plugin.getName() instead to get proper names
		List<String> pluginNames = new LinkedList<String>();
		for (AngeliaPlugin plugin : plugins.values()) {
			pluginNames.add(plugin.getName());
		}
		return pluginNames;
	}

	/**
	 * Starts a plugin and returns the plugin started
	 * 
	 * @param pluginName
	 *          Name of the plugin to start
	 * @param args
	 *          Arguments to pass to the plugin on startup
	 * @return Created plugin instance
	 */
	public AngeliaPlugin executePlugin(String pluginName, String[] args) {
		AngeliaPlugin plugin = getPlugin(pluginName);
		if (plugin == null) {
			connection.getLogger().warn("Plugin " + pluginName + " did not exist");
			return null;
		}
		// create new instance via reflection as we dont want to hand our original out
		try {
			plugin = plugin.getClass().newInstance();
			runningPlugins.add(plugin);
			plugin.setRunning(true);
			plugin.start(connection, args);
			return plugin;
		} catch (InstantiationException | IllegalAccessException e) {
			connection.getLogger().error("Failed to reinstantiate plugin " + plugin.getName(), e);
			return null;
		}
	}

	/**
	 * Finishes all instances of a plugin with the given name
	 * 
	 * @param name
	 *          Name of the plugin to stop
	 * @return How many plugins were stopped
	 */
	public int stopPlugin(String name) {
		int stopped = 0;
		name = name.toLowerCase();
		for (int i = 0; i < runningPlugins.size(); i++) {
			if (runningPlugins.get(i).getName().toLowerCase().equals(name)) {
				runningPlugins.get(i).finish();
				stopped++;
			}
		}
		return stopped;
	}
}
