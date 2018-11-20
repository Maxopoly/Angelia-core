package com.github.maxopoly.angeliacore.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.angeliacore.block.ChunkHolder;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.yaml.ConfigSection;
import com.github.maxopoly.angeliacore.libs.yaml.InvalidYamlFormatException;
import com.github.maxopoly.angeliacore.libs.yaml.YamlParser;

public class GlobalConfig {

	private static final String configPath = "globalConfig.yml";
	private static final String defaultConfigPath = "/globalDefaultConfig.yml";

	private ConfigSection config;
	private File configFile;
	private Logger logger;
	private ServerConnection connection;

	public GlobalConfig(ServerConnection connection, Logger logger, File dataFolder) {
		this.connection = connection;
		this.logger = logger;
		dataFolder.mkdirs();
		configFile = new File(dataFolder, configPath);
		if (!configFile.exists()) {
			saveDefaultConfig();
		}
		reloadConfig();
	}

	public void reloadConfig() {
		try {
			config = YamlParser.loadFromFile(configFile);
		} catch (FileNotFoundException e) {
			logger.warn("Failed to load global config file, it did not exist");
		} catch (InvalidYamlFormatException e) {
			logger.warn("Failed to load global config file, was not of valid yaml format", e);
		} catch (IOException e) {
			logger.warn("IOException occured while loading global config file", e);
		}
	}

	private void saveDefaultConfig() {
		writeFile(getClass().getResourceAsStream(defaultConfigPath), configFile.toPath());
	}
	
	private void writeBackConfig() {
		try {
			YamlParser.writeToFile(config, configFile);
		} catch (IOException e) {
			logger.warn("Failed to write back updated global config", e);
		}
	}

	private boolean writeFile(InputStream source, Path dest) {
		try {
			Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException ex) {
			logger.warn("Failed to save default config, most likely we don't have write perms or the disk is full", ex);
			return false;
		}
	}

	public boolean holdBlockModel() {
		return config.getBoolean("block.holdModel");
	}

	public void setHoldBlockModel(boolean state) {
		if (holdBlockModel() == state) {
			return;
		}
		config.putBoolean("block.holdModel", state);
		ChunkHolder chunkHolder = connection.getChunkHolder();
		if (chunkHolder == null) {
			//connection isnt setup yet
			return;
		}
		chunkHolder.setActivationState(state);
		writeBackConfig();
	}
	
	public long getTokenRefreshDelay() {
		return config.getInt("auth.refreshDelay", 60 * 5 * 1000);
	}
	
	public long getAuthReconnectDelay() {
		return config.getInt("connection.reconnectDelay", 30 * 1000);
	}
	
	public boolean useAutoReconnect() {
		return config.getBoolean("connection.autoReconnect", false);
	}

}
