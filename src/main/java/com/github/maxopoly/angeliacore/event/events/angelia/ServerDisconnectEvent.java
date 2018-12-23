package com.github.maxopoly.angeliacore.event.events.angelia;

import com.github.maxopoly.angeliacore.connection.DisconnectReason;
import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

/**
 * Called when the connection is interrupted for some general reason
 *
 */
public class ServerDisconnectEvent implements AngeliaEvent {

	private final DisconnectReason reason;
	private final long reconnectDelay;
	private final boolean willReconnect;

	public ServerDisconnectEvent(DisconnectReason reason, boolean willReconnect, long reconnectDelay) {
		this.reason = reason;
		this.reconnectDelay = reconnectDelay;
		this.willReconnect = willReconnect;
	}

	/**
	 * @return What caused the disconnect
	 */
	public DisconnectReason getReason() {
		return reason;
	}

	/**
	 * @return How long until reconnecting is attempted
	 */
	public long getReconnectDelay() {
		return reconnectDelay;
	}

	/**
	 * @return Will automatic reconnecting be attempted
	 */
	public boolean willReconnect() {
		return willReconnect;
	}

}
