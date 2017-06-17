package com.github.maxopoly.angeliacore.plugin;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public abstract class AngeliaPlugin {

	private String name;
	private boolean running;
	private boolean finished;

	public AngeliaPlugin(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract void start(ServerConnection connection, String[] args);

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isFinished() {
		return finished;
	}

	public void finish() {
		finished = true;
		tearDown();
	}

	/**
	 * Ends execution of this plugin. Plugins are expected to tear themselves down
	 */
	public void tearDown() {

	}

}
