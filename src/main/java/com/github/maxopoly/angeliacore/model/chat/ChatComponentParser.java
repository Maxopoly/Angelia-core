package com.github.maxopoly.angeliacore.model.chat;

import java.util.LinkedList;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.github.maxopoly.angeliacore.model.chat.hover.HoverEvent;
import com.github.maxopoly.angeliacore.model.chat.hover.ShowEntityHoverEvent;
import com.github.maxopoly.angeliacore.model.chat.hover.ShowItemHoverEvent;
import com.github.maxopoly.angeliacore.model.chat.hover.ShowTextHoverEvent;

public class ChatComponentParser {

	public enum ChatMessageLocation {
		CHAT(0), SYSTEM_MESSAGE(1), ACTION_BAR(2), UNKNOWN(-1);

		private int id;

		private ChatMessageLocation(int id) {
			this.id = id;
		}

		public int getID() {
			return id;
		}

		public static ChatMessageLocation fromID(int id) {
			for (ChatMessageLocation d : ChatMessageLocation.values())
				if (d.getID() == id)
					return d;
			return UNKNOWN;
		}

	}

	private static final boolean defaultBoldValue = false;
	private static final boolean defaultItalicValue = false;
	private static final boolean defaultUnderlinedValue = false;
	private static final boolean defaultStrikethroughValue = false;
	private static final boolean defaultObfuscatedValue = false;
	private static final ChatColor defaultChatColor = ChatColor.DEFAULT;

	public static String getRawText(String json) {
		JSONObject jsonObject = new JSONObject(json);
		return getRecursiveRawText(jsonObject).toString();
	}

	private static StringBuilder getRecursiveRawText(JSONObject json) {
		StringBuilder currentString = new StringBuilder();
		if (json.has("text")) {
			currentString.append(json.getString("text"));
		}
		if (json.has("extra")) {
			for (Object currentJ : json.getJSONArray("extra")) {
				currentString.append(getRecursiveRawText((JSONObject) currentJ));
			}
		}
		return currentString;
	}

	public static List<ChatComponent> parseChatComponent(JSONObject json, Logger logger) {
		return parseChatComponentInternal(json, logger, defaultBoldValue, defaultItalicValue, defaultUnderlinedValue,
				defaultStrikethroughValue, defaultObfuscatedValue, defaultChatColor);
	}

	private static List<ChatComponent> parseChatComponentInternal(JSONObject json, Logger logger, boolean inheritedBold,
			boolean inheritedItalid, boolean inheritedUnderlined, boolean inheritedStrikethrough,
			boolean inheritedObfuscated, ChatColor inheritedColor) {
		List<ChatComponent> result = new LinkedList<ChatComponent>();
		ChatComponent component = null;
		boolean bold = json.optBoolean("bold", inheritedBold);
		boolean italic = json.optBoolean("italic", inheritedItalid);
		boolean underlined = json.optBoolean("underlined", inheritedUnderlined);
		boolean strikethrough = json.optBoolean("strikethrough", inheritedStrikethrough);
		boolean obfuscated = json.optBoolean("obfuscated", inheritedObfuscated);
		ChatColor color;
		if (json.has("color")) {
			color = ChatColor.getColor(json.getString("color"));
		} else {
			color = inheritedColor;
		}
		String insertion = json.optString("insertion", null);
		ClickEvent clickEvent = null;
		if (json.has("clickEvent")) {
			clickEvent = parseClickEvent(json.getJSONObject("clickEvent"), logger);
		}
		HoverEvent hoverEvent = null;
		if (json.has("hoverEvent")) {
			hoverEvent = parseHoverEvent(json.getJSONObject("hoverEvent"), logger);
		}
		if (json.has("text")) {
			String text = json.getString("text");
			component = new TextComponent(bold, italic, underlined, strikethrough, obfuscated, insertion, clickEvent,
					hoverEvent, color, text);
		} else {
			if (json.has("translate")) {
				String translate = json.getString("translate");
				List<ChatComponent> translationComponents = new LinkedList<>();
				if (json.has("with")) {
					for (Object withMember : json.getJSONArray("with")) {
						if (!(withMember instanceof JSONObject)) {
							logger.warn("Found invalid argument with translation object: " + withMember.toString());
							continue;
						}
						translationComponents.addAll(parseChatComponentInternal((JSONObject) withMember, logger, bold,
								italic, underlined, strikethrough, obfuscated, color));
					}
				}
				component = new TranslationComponent(bold, italic, underlined, strikethrough, obfuscated, insertion,
						clickEvent, hoverEvent, color, translate, translationComponents);
			} else {
				if (json.has("keybind")) {
					String keybind = json.getString("keybind");
					component = new KeybindComponent(bold, italic, underlined, strikethrough, obfuscated, insertion,
							clickEvent, hoverEvent, color, keybind);
				} else {
					if (json.has("score")) {
						JSONObject scoreJson = json.optJSONObject("score");
						if (scoreJson == null) {
							logger.warn("Invalid score chat object had no score json: " + json.toString());
						} else {
							String scoreName = scoreJson.optString("name");
							String scoreObjective = scoreJson.optString("objective");
							String scoreValue = scoreJson.optString("value");

							component = new ScoreComponent(bold, italic, underlined, strikethrough, obfuscated,
									insertion, clickEvent, hoverEvent, color, scoreName, scoreObjective, scoreValue);
						}
					}
				}
			}
		}
		if (component == null) {
			logger.warn("Could not parse chat component: " + json.toString());
			return result;
		}
		result.add(component);
		if (json.has("extra")) {
			JSONArray extraArray = json.optJSONArray("extra");
			if (!(extraArray instanceof JSONArray)) {
				logger.warn("Extras were not a json array, could not parse them: " + json.toString());
			} else {
				for (Object extraObj : extraArray) {
					if (!(extraObj instanceof JSONObject)) {
						logger.warn(
								"Extra object was not a json object: " + json.toString() + " ; " + extraObj.toString());
						continue;
					}
					result.addAll(parseChatComponentInternal((JSONObject) extraObj, logger, bold, italic, underlined,
							strikethrough, obfuscated, color));
				}
			}
		}
		return result;
	}

	private static ClickEvent parseClickEvent(JSONObject json, Logger logger) {
		String key = json.optString("action", null);
		if (key == null) {
			logger.warn("Received invalid click event with no action, ignoring it: " + json.toString());
			return null;
		}
		String value = json.optString("value", null);
		if (value == null) {
			logger.warn("Received invalid click event with no value, ignoring it: " + json.toString());
			return null;
		}
		switch (key.toLowerCase()) {
		case "open_url":
			return new ClickEvent(ClickEvent.Action.OPEN_URL, value);
		case "run_command":
			return new ClickEvent(ClickEvent.Action.RUN_COMMAND, value);
		case "suggest_command":
			return new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, value);
		case "change_page":
			return new ClickEvent(ClickEvent.Action.CHANGE_PAGE, value);
		default:
			logger.warn("Unknown key " + key + " for click event, ignoring it: " + json.toString());
			return null;
		}
	}

	private static HoverEvent parseHoverEvent(JSONObject json, Logger logger) {
		String key = json.optString("action", null);
		if (key == null) {
			logger.warn("Received invalid hover event with no action, ignoring it: " + json.toString());
			return null;
		}
		if (!json.has("value")) {
			logger.warn("Received invalid hover event with no value, ignoring it: " + json.toString());
			return null;
		}
		switch (key.toLowerCase()) {
		case "show_text":
			JSONObject textJson = json.optJSONObject("value");
			if (textJson != null) {
				List<ChatComponent> comp = parseChatComponentInternal(textJson, logger, defaultBoldValue,
						defaultItalicValue, defaultUnderlinedValue, defaultStrikethroughValue, defaultObfuscatedValue,
						defaultChatColor);
				return new ShowTextHoverEvent(comp);
			} else {
				String valText = json.getString("value");
				List<ChatComponent> comp = new LinkedList<>();
				comp.add(new TextComponent(defaultBoldValue, defaultItalicValue, defaultUnderlinedValue,
						defaultStrikethroughValue, defaultObfuscatedValue, null, null, null, defaultChatColor,
						valText));
				return new ShowTextHoverEvent(comp);
			}
		case "show_item":
			return new ShowItemHoverEvent(json.getString("value"));
		case "show_entity":
			return new ShowEntityHoverEvent(json.getString("value"));
		default:
			logger.warn("Unknown key " + key + " for hover event, ignoring it: " + json.toString());
			return null;
		}

	}

}
