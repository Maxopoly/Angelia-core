package com.github.maxopoly.angeliacore.event.events.angelia;

import com.github.maxopoly.angeliacore.connection.DisconnectReason;
import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

public class ServerDisconnectEvent implements AngeliaEvent {
	
	private final DisconnectReason reason;
	private final long reconnectDelay;
	private final boolean willReconnect;
	
	public ServerDisconnectEvent(DisconnectReason reason, boolean willReconnect, long reconnectDelay) {
		this.reason = reason;
		this.reconnectDelay = reconnectDelay;
		this.willReconnect = willReconnect;
	}
	
	public DisconnectReason getReason() {
		return reason;
	}
	
	public boolean willReconnect() {
		return willReconnect;
	}
	
	public long getReconnectDelay() {
		return reconnectDelay;
	}

}
