package com.github.maxopoly.angeliacore.event.events;

public class ChatMessageReceivedEvent implements AngeliaEvent {

	private String rawJson;
	private String message;

	public ChatMessageReceivedEvent(String rawJson, String msg) {
		this.rawJson = rawJson;
		this.message = msg;
	}

	/**
	 * @return The raw json which was received for this message
	 */
	public String getRawJSON() {
		return rawJson;
	}

	/**
	 * @return The message received as a normal string without any metadata, highlighting etc. which may have been
	 *         contained in the json
	 */
	public String getMessage() {
		return message;
	}

}
