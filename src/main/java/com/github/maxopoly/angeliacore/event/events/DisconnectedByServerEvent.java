package com.github.maxopoly.angeliacore.event.events;

/**
 * Called if the server intentionally disconnects the client with a proper good bye packet. This is called AFTER closing
 * the server connection properly, so the reconnect behavior can be influenced based on this event
 *
 */
public class DisconnectedByServerEvent implements AngeliaEvent {

	private final String msg;

	public DisconnectedByServerEvent(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return msg;
	}

}
