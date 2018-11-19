package com.github.maxopoly.angeliacore.plugin;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import java.util.List;
import java.util.Map;
import org.apache.commons.cli.Option;
import org.apache.logging.log4j.Logger;

/**
 * A parent class for all plugins which intend to use the Angelia API. All plugins must specify an empty constructor,
 * which calls the constructor of this class with a unique name and the options expected by the plugin
 * 
 * For the creation of options using Apache's Options.Builder is recommended
 * 
 * Additionally so Java's ServiceLoader can find the plugin, it has to have a service loader META-INF entry. This can
 * easily be added with the "@MetaInfServices(AngeliaPlugin.class)" annotation from the maven dependency
 * org.kohsuke.metainf-services:metainf-services:1.7 as demonstrated in my own example plugins
 *
 */
public abstract class AngeliaPlugin {

	private final String name;
	private boolean running;
	private boolean finished;
	private final List<Option> options;

	public AngeliaPlugin(String name) {
		this.name = name;
		this.running = false;
		this.finished = false;
		this.options = createOptions();
	}

	/**
	 * Starts this bot with the parameters given to it. This will only be called if all required options were supplied
	 * and every option used was given the right amount of arguments.
	 * 
	 * The given parameter map will contain all options for which were given, together with the values supplied to them.
	 * If an option was used without supplying any arguments, then the value in the given map will be an empty list for
	 * this option
	 * 
	 * @param connection
	 *            Connections to run the bot on
	 * @param args
	 *            Starting parameters
	 */
	public abstract void start();

	/**
	 * @return A help string describing what this command does
	 */
	public abstract String getHelp();

	/**
	 * Creates all options which this plugin has from scratch, opposed to getOptions, which is just a getter
	 * 
	 * @return This plugins options
	 */
	protected abstract List<Option> createOptions();

	/**
	 * Automatically called when the bot is started to handle passed options
	 */
	protected abstract void parseOptions(ServerConnection connection, Map<String, List<String>> args);

	/**
	 * Plugins are expected to tear themselves down in this method
	 */
	public abstract void tearDown();

	public void printHelp(Logger logger) {
		StringBuilder sb = new StringBuilder(getHelp());
		if (options.size() == 0) {
			sb.append("\nThis bot has no options\n");
		} else {
			sb.append("\n--- Options: ---\n");
			for (Option opt : options) {
				sb.append(" - " + opt.getOpt() + " -- " + opt.getDescription());
				sb.append(opt.isRequired() ? " (Required) " : " (Optional) ");
				sb.append("\n");
			}
		}
		logger.info(sb.toString());
	}

	/**
	 * @return The unique name of this plugin
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets whether this plugin is actively running
	 * 
	 * @param running
	 *            New running state
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Checks whether the plugin is actively running. If the plugin is not running, it may either be paused or completly
	 * finished.
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

	/**
	 * @return All options used by this plugin
	 */
	public List<Option> getOptions() {
		return options;
	}

	/**
	 * When the client automatically reconnects, a new server connection object is created and the old one is trashed.
	 * With that also the old plugin manager and all plugin instances are being trashed. Plugins may provide a copy of
	 * themselves through this method, which will be started up on the new connection. Actions made right before the
	 * disconnect may not have executed and the players location may have changed, so the plugin should recheck that
	 * kinda stuff and possibly roll back.
	 * 
	 * @return Plugin to run on the new connection
	 */
	public abstract AngeliaPlugin transistionToNewConnection(ServerConnection newConnection);

	/**
	 * Ends execution of this plugin
	 */
	public void finish() {
		finished = true;
		tearDown();
	}

}
