package com.github.maxopoly.angeliacore.plugin;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

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

	public AngeliaPlugin getPlugin(String name) {
		return plugins.get(name.toLowerCase());
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
			Map<String, List<String>> options = parseOptions(plugin, args);
			if (options == null) {
				return null;
			}
			plugin.parseOptions(connection, options);
			if (plugin.isFinished()) {
				// option parsing might have failed
				return null;
			}
			plugin.start();
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

	/**
	 * When a new server connection is made (after reconnecting), a new plugin manager is created along with it. This
	 * method passes the old plugin managers plugins to the new one. The actual plugins passed are not the ones previously
	 * used, but instead copies specifically made for use in a new connection and to handle errors coming along with that.
	 *
	 * @param replacement
	 *          new plugin manager
	 */
	public void passPluginsOver(ServerConnection newConnection) {
		PluginManager replacement = newConnection.getPluginManager();
		for (AngeliaPlugin plugin : runningPlugins) {
			if (plugin.isFinished()) {
				continue;
			}
			AngeliaPlugin replacePlugin = plugin.transistionToNewConnection(newConnection);
			replacement.runningPlugins.add(replacePlugin);
			replacePlugin.start();
			replacePlugin.setRunning(plugin.isRunning());
		}
	}

	private Map<String, List<String>> parseOptions(AngeliaPlugin plugin, String[] args) {
		CommandLineParser parser = new DefaultParser();
		Options options = new Options();
		for (Option opt : plugin.getOptions()) {
			options.addOption(opt);
		}
		CommandLine cmd;
		try {
			cmd = parser.parse(options, args, true);
		} catch (MissingArgumentException e) {
			Option opt = e.getOption();
			connection.getLogger().warn(
					"An incorrect amount of argument(s) was supplied for the option " + opt.getArgName() + ". Run \"helpplugin "
							+ plugin.getName() + "\" for more information on how to use this command");
			return null;
		} catch (MissingOptionException e) {
			List<String> missingOptions = e.getMissingOptions();
			StringBuilder sb = new StringBuilder();
			for (String opt : missingOptions) {
				sb.append(opt + " ");
			}
			connection.getLogger().warn(
					"The required argument(s) " + sb.toString() + "were not supplied. Run \"helpplugin " + plugin.getName()
							+ "\" for more information on how to use this command");
			return null;
		} catch (UnrecognizedOptionException e) {
			connection.getLogger().warn(
					"The supplied option " + e.getOption() + " could not be recognized. Run \"helpplugin " + plugin.getName()
							+ "\" for more information on how to use this command");
			return null;
		} catch (ParseException e) {
			connection.getLogger().error("An unknown exception occured while trying to parse arguments", e);
			return null;
		}
		Map<String, List<String>> parsedValues = new HashMap<String, List<String>>();
		for (Option option : cmd.getOptions()) {
			if (option.hasArg()) {
				parsedValues.put(option.getOpt(), Arrays.asList(cmd.getOptionValues(option.getOpt())));
			} else {
				parsedValues.put(option.getOpt(), new LinkedList<String>());
			}
		}
		return parsedValues;
	}
}
