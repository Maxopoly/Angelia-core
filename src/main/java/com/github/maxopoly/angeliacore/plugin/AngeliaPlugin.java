package com.github.maxopoly.angeliacore.plugin;

import java.io.File;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.yaml.config.PluginConfig;
/**
 * A parent class for all plugins which intend to use the Angelia API. All
 * plugins must specify an empty constructor, which calls the constructor of
 * this class with a unique name and the options expected by the plugin
 * 
 * For the creation of options using Apache's Options.Builder is recommended
 * 
 * Additionally so Java's ServiceLoader can find the plugin, it has to have a
 * service loader META-INF entry. This can easily be added with the
 * "@MetaInfServices(AngeliaPlugin.class)" annotation from the maven dependency
 * org.kohsuke.metainf-services:metainf-services:1.7 as demonstrated in my own
 * example plugins
 *
 */
public abstract class AngeliaPlugin {

	protected boolean running = false;
	protected boolean finished = false;
	protected ServerConnection connection;
	private File dataFolder;
	private PluginConfig config;

	protected AngeliaPlugin() {		
	}

	/**
	 * Starts this bot with the parameters given to it. This will only be called if
	 * all required options were supplied and every option used was given the right
	 * amount of arguments.
	 * 
	 * The given parameter map will contain all options for which were given,
	 * together with the values supplied to them. If an option was used without
	 * supplying any arguments, then the value in the given map will be an empty
	 * list for this option
	 * 
	 * @param connection Connections to run the bot on
	 * @param args       Starting parameters
	 */
	public abstract void start();

	/**
	 * Called when the plugin is stopped through external interference. This can be
	 * explicitly requested by the user or another plugin or can be initiated
	 * automatically when then entire instance is shutdown
	 */
	public abstract void stop();

	/**
	 * Sets whether this plugin is actively running
	 * 
	 * @param running New running state
	 */
	void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Checks whether the plugin is actively running. If the plugin is not running,
	 * it may either be paused or completly finished.
	 * 
	 * @return Whether this plugin is actively running
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * @return Whether execution of this plugin is completly done
	 */
	public boolean isFinished() {
		return finished;
	}

	public String getName() {
		AngeliaLoad pluginAnnotation = getPluginAnnotation();
		if (pluginAnnotation == null) {
			return null;
		}
		return pluginAnnotation.name();
	}

	public String getDescription() {
		AngeliaLoad pluginAnnotation = getPluginAnnotation();
		if (pluginAnnotation == null) {
			return null;
		}
		return pluginAnnotation.description();
	}

	public String getVersion() {
		AngeliaLoad pluginAnnotation = getPluginAnnotation();
		if (pluginAnnotation == null) {
			return null;
		}
		return pluginAnnotation.version();
	}

	private AngeliaLoad getPluginAnnotation() {
		Class<? extends AngeliaPlugin> pluginClass = this.getClass();
		return pluginClass.getAnnotation(AngeliaLoad.class);
	}
	
	void setConnection(ServerConnection connection) {
		this.connection = connection;
		String name = getName();
		if (name == null) {
			throw new IllegalStateException("Plugin was initialized without valid name");
		}
		this.dataFolder = new File(connection.getDataFolder(), name);
		this.config = new PluginConfig(connection.getLogger(), new File(dataFolder, "config.yml"));
	}
	
	void loadConfig() {
		config.reloadConfig();
	}
	
	public File getDataFolder() {
		return dataFolder;
	}
	
	protected PluginConfig getConfig() {
		return config;
	}
}
