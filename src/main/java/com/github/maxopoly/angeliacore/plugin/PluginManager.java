package com.github.maxopoly.angeliacore.plugin;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterLoad;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParsingFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PluginManager {

	private ServerConnection connection;
	private PluginService pluginService;
	private Map<String, AngeliaPlugin> plugins;
	private List<AngeliaPlugin> runningPlugins;
	private ParameterParsingFactory parsingFactory;

	public PluginManager(ServerConnection connection) {
		this.plugins = new HashMap<String, AngeliaPlugin>();
		this.runningPlugins = new LinkedList<AngeliaPlugin>();
		this.pluginService = PluginService.getInstance(connection.getLogger());
		this.connection = connection;
		this.parsingFactory = new ParameterParsingFactory();
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
		Class<? extends AngeliaPlugin> pluginClass = plugin.getClass();
		AngeliaLoad pluginAnnotation = pluginClass.getAnnotation(AngeliaLoad.class);
		if (pluginAnnotation == null) {
			connection.getLogger()
					.warn("Plugin " + plugin.getClass().getName() + " had no AngeliaLoad annotation, it was ignored");
			return;
		}
		Constructor<?> constr = pluginClass.getConstructors()[0];
		if (constr.getParameterCount() != 0) {
			connection.getLogger()
					.warn("Plugin " + plugin.getClass().getName() + " had no default constructor, it was ignored");
			return;
		}
		constr.setAccessible(true);
		String name = pluginAnnotation.name();
		if (plugins.containsKey(name.toLowerCase())) {
			connection.getLogger().warn("Plugin " + name + " was already registered, did not register again");
			return;
		}
		connection.getLogger().info("Registering plugin " + name);
		plugins.put(name.toLowerCase(), plugin);
	}

	public synchronized List<String> getAvailablePlugins() {
		// map values are all lower case, so we use plugin.getName() instead to get
		// proper names
		List<String> pluginNames = new LinkedList<String>();
		for (AngeliaPlugin plugin : plugins.values()) {
			pluginNames.add(plugin.getName());
		}
		return pluginNames;
	}

	/**
	 * Starts a plugin and returns the plugin started
	 *
	 * @param pluginName Name of the plugin to start
	 * @param args       Arguments to pass to the plugin on startup
	 * @return Created plugin instance
	 */
	public AngeliaPlugin executePlugin(String pluginName, Map<String, String> args) {
		AngeliaPlugin plugin = getPlugin(pluginName);
		if (plugin == null) {
			connection.getLogger().warn("Plugin " + pluginName + " did not exist");
			return null;
		}
		// create new instance via reflection as we dont want to hand our original out
		try {
			plugin = (AngeliaPlugin) (plugin.getClass().getConstructors()[0].newInstance());
			plugin.setConnection(connection);
			plugin.loadConfig();
			plugin.setRunning(true);
			boolean worked = loadOptions(plugin, args);
			if (!worked) {
				connection.getLogger().warn("Plugin could not be launched, because option parsing failed");
				return null;
			}
			plugin.start();
			if (plugin.isFinished()) {
				// may have disabled itself already in the start method, in this case it must
				// report errors itself
				return null;
			}
			runningPlugins.add(plugin);
			return plugin;
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			connection.getLogger().error("Failed to reinstantiate plugin " + plugin.getName(), e);
			return null;
		}
	}

	/**
	 * Finishes all instances of a plugin with the given name
	 *
	 * @param name Name of the plugin to stop
	 * @return How many plugins were stopped
	 */
	public int stopPlugin(String name) {
		int stopped = 0;
		name = name.toLowerCase();
		Iterator<AngeliaPlugin> iterator = runningPlugins.iterator();
		while (iterator.hasNext()) {
			AngeliaPlugin plugin = iterator.next();
			if (plugin.getName().toLowerCase().equals(name)) {
				plugin.stop();
				iterator.remove();
				stopped++;
			}
		}
		return stopped;
	}

	/**
	 * When a new server connection is made (after reconnecting), a new plugin
	 * manager is created along with it. This method passes the old plugin managers
	 * plugins to the new one.
	 *
	 * @param replacement new plugin manager
	 */
	public void passPluginsOver(ServerConnection newConnection) {
		PluginManager replacement = newConnection.getPluginManager();
		for (AngeliaPlugin plugin : runningPlugins) {
			replacement.runningPlugins.add(plugin);
			plugin.setConnection(newConnection);
		}
	}

	private boolean loadOptions(AngeliaPlugin plugin, Map<String, String> args) {
		Class<? extends AngeliaPlugin> pluginClass = plugin.getClass();
		for (Field field : pluginClass.getDeclaredFields()) {
			ParameterLoad annot = field.getAnnotation(ParameterLoad.class);
			if (annot == null) {
				continue;
			}
			String identifier;
			if (annot.id().equals("")) {
				identifier = field.getName();
			} else {
				identifier = annot.id();
			}
			String rawValue = args.get(identifier);
			Class<?> fieldClass = field.getType();
			ParameterParser<?> parser = parsingFactory.getParser(fieldClass);
			if (parser == null) {
				connection.getLogger().warn("No parser available for field " + field.getName() + " in "
						+ plugin.getClass().getName() + ", it not loaded");
				if (annot.isRequired()) {
					connection.getLogger().warn("Required value " + identifier + " could not be parsed");
					return false;
				}
				continue;
			}
			field.setAccessible(true);
			Object inputValue;
			Object yamlValue;
			if (rawValue == null) {
				inputValue = null;
			} else {
				try {
					inputValue = parser.parse(rawValue);
				} catch (InvalidParameterValueException e) {
					connection.getLogger()
							.warn("Value " + rawValue + "could not be parsed for type " + fieldClass.getName());
					inputValue = null;
				}
			}
			if (!annot.configId().equals("")) {
				yamlValue = plugin.getConfig().getConfig().retrieve(annot.configId(), parser.getClassParsed(), false);
			}
			else {
				yamlValue = null;
			}
			Object valueToUse = annot.policy().parse(inputValue, yamlValue);
			if (annot.isRequired() && valueToUse == null) {
				connection.getLogger().warn("Required value " + identifier + " was null");
				return false;
			}
			if (fieldClass.isPrimitive() && valueToUse == null) {
				connection.getLogger()
						.warn("Null value for primitive type " + fieldClass.getName() + " was not allowed");
				if (annot.isRequired()) {
					connection.getLogger().warn("Required value " + identifier + " could not be parsed");
					return false;
				}
				continue;
			}
			try {
				field.set(plugin, inputValue);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				connection.getLogger().warn("Could not set field " + identifier, e);
				return false;
			}
		}
		return true;
	}
}
