package com.github.maxopoly.angeliacore.libs.yaml.config;

import java.io.File;

import org.apache.logging.log4j.Logger;

import com.github.maxopoly.angeliacore.libs.yaml.ConfigSection;

public class PluginConfig extends YAMLFileConfig {

	public PluginConfig(Logger logger, File file) {
		super(logger, file);
	}

	public ConfigSection getConfig() {
		return config;
	}
}
