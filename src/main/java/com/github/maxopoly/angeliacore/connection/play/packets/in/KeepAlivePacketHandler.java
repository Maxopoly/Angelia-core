package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.KeepAlivePacket;
import java.io.IOException;

public class KeepAlivePacketHandler extends AbstractIncomingPacketHandler {

	public KeepAlivePacketHandler(ServerConnection connection) {
		super(connection, 0x1F);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		// keep alive are basically just a random long sent by the server. The server expects us to send the same int
		// back
		long number;
		try {
			number = packet.readLong();
		} catch (EndOfPacketException e1) {
			connection.getLogger().error("Failed to parse keep alive", e1);
			return;
		}
		try {
			connection.sendPacket(new KeepAlivePacket(number));
			connection.getIncomingPlayPacketHandler().updateKeepAlive();
		} catch (IOException e) {
			connection.getLogger().error("Failed to reply to keep alive", e);
		}
	}
}
