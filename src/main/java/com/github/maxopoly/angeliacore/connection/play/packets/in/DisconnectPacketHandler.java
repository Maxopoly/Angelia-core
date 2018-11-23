package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.DisconnectReason;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.DisconnectedByServerEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

public class DisconnectPacketHandler extends AbstractIncomingPacketHandler {

	public DisconnectPacketHandler(ServerConnection connection) {
		super(connection, 0x1A);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			String msg = packet.readString();
			connection.getLogger().error("Server disconnected with reason: " + msg);
			connection.close(DisconnectReason.Server_Disconnected_Intentionally);
			connection.getEventHandler().broadcast(new DisconnectedByServerEvent(msg));
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse disconnect msg", e);
		}
	}
}
