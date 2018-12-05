package com.github.maxopoly.angeliacore.event.events.angelia;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

/**
 * Will be called after reconnecting on the event listener of the new
 * connection. Only listeners with autoTransfer enabled will see this event
 *
 */
public class ReconnectEvent implements AngeliaEvent {

	private ServerConnection oldConnection;
	private ServerConnection newConnection;

	public ReconnectEvent(ServerConnection oldConnection, ServerConnection newConnection) {
		this.oldConnection = oldConnection;
		this.newConnection = newConnection;
	}

	/**
	 * @return Previous connection used. This will always be a connection that was
	 *         fully established before, not one that failed during the reconnecting
	 *         processs
	 */
	public ServerConnection getOldConnection() {
		return oldConnection;
	}

	/**
	 * @return New connection to use. At the time of this event this connection is
	 *         already fully setup
	 */
	public ServerConnection getNewConnection() {
		return newConnection;
	}

}
