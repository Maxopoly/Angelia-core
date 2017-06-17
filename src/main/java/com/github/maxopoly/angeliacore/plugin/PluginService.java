package com.github.maxopoly.angeliacore.plugin;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
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
		for (AngeliaPlugin plugin : loader) {
			plugins.add(plugin);
		}
		return plugins;
	}

	private URLClassLoader addPluginFolderToClassPath() {
		File dir = new File(pluginFolder);
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
				e.printStackTrace();
			}
		}
		return URLClassLoader.newInstance(urlsList.toArray(new URL[] {}), Thread.currentThread().getContextClassLoader());
	}
}
