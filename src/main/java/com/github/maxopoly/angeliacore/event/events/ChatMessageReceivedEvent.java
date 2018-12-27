package com.github.maxopoly.angeliacore.event.events;

import java.util.List;

import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import com.github.maxopoly.angeliacore.model.chat.ChatComponent;
import com.github.maxopoly.angeliacore.model.chat.ChatComponentParser;

public class ChatMessageReceivedEvent implements AngeliaEvent {

	private String rawJson;
	private String message;
	private Logger logger;
	private List<ChatComponent> components;

	public ChatMessageReceivedEvent(String rawJson, String msg, Logger logger) {
		this.rawJson = rawJson;
		this.message = msg;
		this.logger = logger;
	}

	/**
	 * @return The message received as a normal string without any metadata,
	 *         highlighting etc. which may have been contained in the json
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @return The raw json which was received for this message
	 */
	public String getRawJSON() {
		return rawJson;
	}

	/**
	 * @return Chat in the form of chat components
	 */
	public List<ChatComponent> getParsedChat() {
		if (components == null) {
			// only parse once and only if needed
			components = ChatComponentParser.parseChatComponent(new JSONObject(rawJson), logger);
		}
		return components;
	}

}
