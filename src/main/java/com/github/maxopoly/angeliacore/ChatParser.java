package com.github.maxopoly.angeliacore;

import org.json.JSONObject;

public class ChatParser {

	public static String getRawText(String json) {
		JSONObject jsonObject = new JSONObject(json);
		StringBuilder sb = new StringBuilder();
		if (jsonObject.has("text")) {
			sb.append(jsonObject.getString("text"));
		}
		if (jsonObject.has("extra")) {
			for (Object currentJ : jsonObject.getJSONArray("extra")) {
				JSONObject currentObject = (JSONObject) currentJ;
				if (currentObject.has("text")) {
					sb.append(currentObject.getString("text"));
				}
			}
		}
		return sb.toString();
	}

}
