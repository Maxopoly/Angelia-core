package com.github.maxopoly.angeliacore.util;

import org.json.JSONObject;

public class ChatParser {

	public static String getRawText(String json) {
		JSONObject jsonObject = new JSONObject(json);
		return getRecursiveRawText(new StringBuilder(), jsonObject).toString();
	}

	private static StringBuilder getRecursiveRawText(StringBuilder currentString, JSONObject json) {
		if (json.has("text")) {
			currentString.append(json.getString("text"));
		}
		if (json.has("extra")) {
			for (Object currentJ : json.getJSONArray("extra")) {
				currentString.append(getRecursiveRawText(currentString, (JSONObject) currentJ));
			}
		}
		return currentString;
	}

}
