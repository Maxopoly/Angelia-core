package com.github.maxopoly.angeliacore.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import org.apache.logging.log4j.Logger;

public class PluginService {

	private static PluginService instance;
	private ServiceLoader<AngeliaPlugin> loader;
	private URLClassLoader classLoader;
	private static final String pluginFolder = "plugins";
	private Logger logger;

	private PluginService(Logger logger) {
		this.logger = logger;
		classLoader = addPluginFolderToClassPath();
		reloadJars();
	}

	public static synchronized PluginService getInstance(Logger logger) {
		if (instance == null) {
			instance = new PluginService(logger);
		}
		return instance;
	}

	public synchronized void reloadJars() {
		this.loader = ServiceLoader.load(AngeliaPlugin.class, classLoader);
	}

	public synchronized List<AngeliaPlugin> getAvailablePlugins() {
		List<AngeliaPlugin> plugins = new LinkedList<AngeliaPlugin>();
		Iterator<AngeliaPlugin> iter = loader.iterator();
		while (iter.hasNext()) {
			try {
				plugins.add(iter.next());
			} catch (ServiceConfigurationError e) {
				logger.warn("Failed to load a plugin, here's some debug info for its dev: ", e);
				continue;
			}
		}
		return plugins;
	}

	private URLClassLoader addPluginFolderToClassPath() {
		File dir = new File(pluginFolder);
		if (!dir.exists()) {
			dir.mkdir();
		}
		List<File> jars = new LinkedList<File>();
		for (File f : dir.listFiles()) {
			if (f.getName().endsWith(".jar")) {
				jars.add(f);
			}
		}
		List<URL> urlsList = new ArrayList<>();
		for (File file : jars) {
			try {
				urlsList.add(file.toURI().toURL());
			} catch (MalformedURLException e) {
				logger.error("Failed to load jar, invalid path", e);
			}
		}
		return URLClassLoader.newInstance(urlsList.toArray(new URL[] {}),
				Thread.currentThread().getContextClassLoader());
	}
}
