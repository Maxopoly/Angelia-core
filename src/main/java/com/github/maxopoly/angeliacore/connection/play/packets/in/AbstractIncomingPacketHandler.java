package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

public abstract class AbstractIncomingPacketHandler {

	private int idHandled;
	// connection is saved so a reply can be sent right away if needed
	protected ServerConnection connection;

	public AbstractIncomingPacketHandler(ServerConnection connection, int idHandled) {
		this.idHandled = idHandled;
		this.connection = connection;
	}

	public int getIDHandled() {
		return idHandled;
	}

	public abstract void handlePacket(ReadOnlyPacket packet);

}
