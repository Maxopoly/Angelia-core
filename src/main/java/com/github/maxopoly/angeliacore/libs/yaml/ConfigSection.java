package com.github.maxopoly.angeliacore.libs.yaml;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Wrapper for a configuration parsed from a YAML file. Conceptually very
 * similar to Bukkits ConfigurationSection
 * (https://hub.spigotmc.org/javadocs/bukkit/org/bukkit/configuration/ConfigurationSection.html)
 * 
 * All entries in a ConfigSection have a key and are either values or
 * ConfigSections themselves. Keys must be unique within a single ConfigSection,
 * trying to insert an already existing key will overwrite the preexisting value
 * behind the key
 * 
 * For all supported value types V, the following methods are provided:
 * 
 * putV(String path, V value);
 * 
 * hasV(String path);
 * 
 * getV(String path);
 * 
 * getV(String path, V default)
 * 
 * Paths are always relative from the section they are used on. They may refer
 * to deeper levels by separating the individual identifiers with '.'. For
 * example: 'block.color.r' retrieves a value with the key 'r' in the section
 * 'color', which is in the section 'block', where block is a section within the
 * section the call is made on
 * 
 * putV(String path, V value) will always insert the given value, creating new
 * sections if needed and overriding any existing values (of possibly different
 * type) at the same path. Will never throw run time exceptions.
 * 
 * hasV(String path) checks if a value of type V exists at the given path and
 * returns true only if it does. Will never throw run time exceptions.
 * 
 * getV(String path) retrieves a value of type V at the given path. May throw
 * YamlEntryNotFoundException if no such path exists or YamlEntryTypeException
 * if the path exists, but is not of type V. If it does not throw an exception,
 * it always returns a non-null value
 * 
 * getV(String path, V default) retrieves a value of type V at the given path.
 * If no such path is found or if the value at the path is of a different type,
 * the given default value is returned instead. Will never throw run time
 * exceptions.
 */
public class ConfigSection {

	private Map<String, Object> mapping;
	private String location;

	public ConfigSection() {
		this("");
	}

	ConfigSection(String location) {
		mapping = new HashMap<>();
		this.location = location;
	}

	public ConfigSection createConfigSection(String tag) {
		ConfigSection map = new ConfigSection(location + "." + tag);
		internalPut(tag, map);
		return map;
	}

	public Map<?, ?> dump() {
		Map<String, Object> result = new HashMap<>();
		for (Entry<String, Object> entry : mapping.entrySet()) {
			Object objectToPut;
			if (entry.getValue() instanceof ConfigSection) {
				objectToPut = ((ConfigSection) entry.getValue()).dump();
			} else {
				objectToPut = entry.getValue();
			}
			result.put(entry.getKey(), objectToPut);
		}
		return result;
	}

	public boolean getBoolean(String key) {
		return retrieve(key, Boolean.class, true);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		Boolean bool = retrieve(key, Boolean.class, false);
		if (bool == null) {
			return defaultValue;
		}
		return bool;
	}

	/**
	 * Retrieves a configuration section by its path. Note that if a value (not
	 * config section) with the given path exists, null will still be returned
	 * 
	 * @param key Path to the configuration section
	 * @return Configuration section with the given path or null if no such section
	 *         exists
	 */
	public ConfigSection getConfigSection(String key) {
		return retrieve(key, ConfigSection.class, false);
	}

	public int getInt(String key) {
		return retrieve(key, Integer.class, true);
	}

	public int getInt(String key, int defaultValue) {
		Integer integer = retrieve(key, Integer.class, false);
		if (integer == null) {
			return defaultValue;
		}
		return integer;
	}

	public String getString(String key) {
		return retrieve(key, String.class, true);
	}

	public String getString(String key, String defaultValue) {
		String string = retrieve(key, String.class, false);
		if (string == null) {
			return defaultValue;
		}
		return string;
	}

	public boolean hasBoolean(String key) {
		return retrieve(key, Boolean.class, false) != null;
	}

	public boolean hasInt(String key) {
		return retrieve(key, Integer.class, false) != null;
	}

	public boolean hasString(String key) {
		return retrieve(key, String.class, false) != null;
	}

	private <T> void internalPut(String tag, T o) {
		put(splitPath(tag), o);
	}

	private String print(String prefix) {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, Object> entry : mapping.entrySet()) {
			if (entry.getValue() instanceof ConfigSection) {
				ConfigSection inner = (ConfigSection) entry.getValue();
				sb.append(inner.print(prefix + entry.getKey() + "."));
			} else {
				sb.append(prefix + entry.getKey() + "("
						+ (entry.getValue() != null ? entry.getValue().getClass().getName() : "null") + ")): "
						+ entry.getValue() + "\n");
			}
		}
		return sb.toString();
	}

	private <T> void put(List<String> tags, T o) {
		String key = tags.get(0);
		if (tags.size() > 1) {
			ConfigSection config = getConfigSection(key);
			if (config == null) {
				config = createConfigSection(key);
			}
			tags.remove(0);
			config.put(tags, o);
		} else {
			mapping.put(key, o);
		}
	}

	/**
	 * Only usable during deserialization
	 */
	void put(String key, Object o) {
		internalPut(key, o);
	}

	public void putBoolean(String path, boolean value) {
		internalPut(path, value);
	}

	public void putInt(String path, int value) {
		internalPut(path, value);
	}

	public void putString(String path, String value) {
		internalPut(path, value);
	}

	@SuppressWarnings("unchecked")
	private <T> T retrieve(List<String> keyList, Class<T> parameterClass, boolean throwException) {
		if (keyList.size() > 1) {
			ConfigSection section = getConfigSection(keyList.get(0));
			if (section == null) {
				if (throwException) {
					throw new YamlEntryNotFoundException("Section " + keyList.get(0) + " not found in " + location);
				} else {
					return null;
				}
			}
			keyList.remove(0);
			return section.retrieve(keyList, parameterClass, throwException);
		}
		String key = keyList.get(0);
		Object o = mapping.get(key);
		if (o == null) {
			if (throwException) {
				throw new YamlEntryNotFoundException("Could not find entry " + key + " in " + location);
			} else {
				return null;
			}
		}
		if (!parameterClass.isInstance(o)) {
			if (throwException) {
				throw new YamlEntryTypeException("Tried to retrieve '" + key + "' with type " + parameterClass.getName()
						+ ", but type was " + o.getClass().getName() + " at " + location);
			} else {
				return null;
			}
		}
		return (T) o;
	}

	public <T> T retrieve(String key, Class<T> parameterClass, boolean throwException) {
		return retrieve(splitPath(key), parameterClass, throwException);
	}

	private List<String> splitPath(String path) {
		List<String> result = new LinkedList<>();
		for (String s : path.split("\\.")) {
			result.add(s);
		}
		return result;
	}

	public String toString() {
		return print("");
	}

}
