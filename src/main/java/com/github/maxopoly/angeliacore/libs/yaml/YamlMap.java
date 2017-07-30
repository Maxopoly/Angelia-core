package com.github.maxopoly.angeliacore.libs.yaml;

import java.util.HashMap;
import java.util.Map;

public class YamlMap {

	public void put(String tag, Object o) {

	}

	public YamlMap createMap(String tag) {
		YamlMap map = new YamlMap();
		put(tag, map);
		return map;
	}

	public Map dump() {
		// TODO
		return new HashMap();
	}

}
