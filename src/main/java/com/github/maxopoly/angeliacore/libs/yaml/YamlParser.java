package com.github.maxopoly.angeliacore.libs.yaml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

public class YamlParser {

	private static final String BLANK_CONFIG = "{}\n";
	private static final Yaml yaml = new Yaml();

	public static void writeToFile(YamlMap map, File f) throws IOException {
		FileOutputStream fos = new FileOutputStream(f);
		byte[] dataToWrite = saveToString(map).getBytes(StandardCharsets.UTF_8);
		int steps = 1024;
		try {
			int i = 0;
			while (i < dataToWrite.length) {
				int len = Math.min(steps, dataToWrite.length - i);
				fos.write(dataToWrite, i, len);
				i += len;
			}
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				// its fine
			}
		}
	}

	public static YamlMap loadFromFile(File f) throws FileNotFoundException, InvalidYamlFormatException, IOException {
		return loadFromString(getFileContent(f));
	}

	private static String getFileContent(File file) throws FileNotFoundException, IOException {
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		StringBuilder sb = new StringBuilder();
		try {
			String line;

			while ((line = input.readLine()) != null) {
				sb.append(line);
				sb.append('\n');
			}
		} finally {
			input.close();
		}
		return sb.toString();
	}

	public static YamlMap loadFromString(String rawYaml) throws InvalidYamlFormatException {
		Map input;
		try {
			input = (Map) yaml.load(rawYaml);
		} catch (YAMLException e) {
			throw new InvalidYamlFormatException(e.getMessage());
		} catch (ClassCastException e) {
			throw new InvalidYamlFormatException("Tried to parse yaml, but top level was not a map");
		}
		if (input != null) {
			YamlMap superYaml = new YamlMap();
			convertMapsToSections(input, superYaml);
			return superYaml;
		}
		return null;
	}

	public static String saveToString(YamlMap yamlMap) {
		String dump = yaml.dump(yamlMap.dump());

		if (dump.equals(BLANK_CONFIG)) {
			dump = "";
		}

		return dump;
	}

	private static void convertMapsToSections(Map<?, ?> input, YamlMap section) {
		for (Map.Entry<?, ?> entry : input.entrySet()) {
			String key = entry.getKey().toString();
			Object value = entry.getValue();

			if (value instanceof Map) {
				convertMapsToSections((Map<?, ?>) value, section.createMap(key));
			} else {
				section.put(key, value);
			}
		}
	}
}
