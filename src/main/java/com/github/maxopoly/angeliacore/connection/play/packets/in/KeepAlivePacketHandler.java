package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.KeepAlivePacket;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

import java.io.IOException;

public class KeepAlivePacketHandler extends AbstractIncomingPacketHandler {

	public KeepAlivePacketHandler(ServerConnection connection) {
		super(connection, 0x1F);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		// keep alive are basically just a random var int sent by the server. The server expects us to send the same int
		// back
		int number = packet.readVarInt();
		try {
			connection.sendPacket(new KeepAlivePacket(number));
			connection.getIncomingPlayPacketHandler().updateKeepAlive();
		} catch (IOException e) {
			connection.getLogger().error("Failed to reply to keep alive", e);
		}
	}
}
