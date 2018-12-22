package com.github.maxopoly.angeliacore.libs.yaml.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.angeliacore.libs.yaml.ConfigSection;
import com.github.maxopoly.angeliacore.libs.yaml.InvalidYamlFormatException;
import com.github.maxopoly.angeliacore.libs.yaml.YamlParser;

public class YAMLFileConfig {

	protected ConfigSection config;
	protected File configFile;
	protected Logger logger;
	private String defaultConfigPath;
	private boolean isDirty;

	public YAMLFileConfig(Logger logger, File file) {
		this(logger, file, null);
	}

	public YAMLFileConfig(Logger logger, File file, String defaultConfigPath) {
		if (file == null) {
			throw new IllegalArgumentException("Config file may not be null");
		}
		this.configFile = file;
		this.logger = logger;
		this.defaultConfigPath = defaultConfigPath;
	}

	/**
	 * Checks whether a default config path was specified, not if the file behind it
	 * actually exists
	 * 
	 * @return True if a non-null default config path was specified, false otherwise
	 */
	public boolean hasDefaultConfig() {
		return defaultConfigPath != null;
	}

	/**
	 * @return Whether there are changes that have to be written back to the file
	 */
	public boolean isDirty() {
		return isDirty;
	}

	/**
	 * Reloads the ConfigSection from the flat file
	 */
	public void reloadConfig() {
		try {
			config = YamlParser.loadFromFile(configFile);
			return;
		} catch (FileNotFoundException e) {
			logger.warn("Failed to load config file " + configFile.getAbsolutePath() + ", it did not exist");
			logger.info("Creating empty config file" + configFile.getAbsolutePath());
			try {
				configFile.getParentFile().mkdirs();
				configFile.createNewFile();
			} catch (IOException e1) {
				logger.warn("Failed to create config file " + configFile.getAbsolutePath()
						+ ", did you setup permissions wrong?");
			}
		} catch (InvalidYamlFormatException e) {
			logger.warn("Failed to load config file " + configFile.getAbsolutePath() + ", was not of valid yaml format",
					e);
		} catch (IOException e) {
			logger.warn("IOException occured while loading config file " + configFile.getAbsolutePath(), e);
		}
		config = null;
	}

	/**
	 * Writes the current state of the ConfigSection in memory back to the YAML flat
	 * file
	 */
	public void saveConfig() {
		try {
			YamlParser.writeToFile(config, configFile);
		} catch (IOException e) {
			logger.warn("Failed to write back updated config " + configFile.getAbsolutePath(), e);
		}
	}

	public void saveDefaultConfig() {
		if (defaultConfigPath == null) {
			throw new IllegalStateException("Could not save default config, none was specified");
		}
		writeFile(getClass().getResourceAsStream(defaultConfigPath), configFile.toPath());
	}

	/**
	 * Sets whether there are changes to the in memory ConfigSection that have to be
	 * written back
	 * 
	 * @param dirty True if there are changes, false otherwise
	 */
	public void setDirty(boolean dirty) {
		this.isDirty = dirty;
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

}
