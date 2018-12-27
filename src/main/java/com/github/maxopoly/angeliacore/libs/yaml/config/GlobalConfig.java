package com.github.maxopoly.angeliacore.libs.yaml.config;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.block.ChunkHolder;

public class GlobalConfig extends YAMLFileConfig {

	private ServerConnection connection;

	public GlobalConfig(ServerConnection connection, Logger logger, File dataFolder) {
		super(logger, new File(dataFolder, "globalConfig.yml"), "/globalDefaultConfig.yml");
		this.connection = connection;
		if (!configFile.exists()) {
			logger.info("Global config file did not exist, creating it");
			saveDefaultConfig();
		}
		reloadConfig();
	}

	public long getAuthReconnectDelay() {
		return config.getInt("connection.reconnectDelay", 30 * 1000);
	}

	public long getTokenRefreshDelay() {
		return config.getInt("auth.refreshDelay", 60 * 5 * 1000);
	}

	public boolean holdBlockModel() {
		return config.getBoolean("block.holdModel", false);
	}

	public void setHoldBlockModel(boolean state) {
		if (holdBlockModel() == state) {
			return;
		}
		config.putBoolean("block.holdModel", state);
		ChunkHolder chunkHolder = connection.getChunkHolder();
		if (chunkHolder == null) {
			// connection isnt setup yet
			return;
		}
		chunkHolder.setActivationState(state);
		setDirty(true);
	}

	public boolean useAutoReconnect() {
		return config.getBoolean("connection.autoReconnect", false);
	}

}
