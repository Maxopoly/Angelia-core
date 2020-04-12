package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

public class TimeUpdatePacketHandler extends AbstractIncomingPacketHandler {

	public TimeUpdatePacketHandler(ServerConnection connection) {
		super(connection, 0x47);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			long worldAge = packet.readLong();
			long timeOfDay = packet.readLong();
			connection.getWorldStatusHandler().updateWorldTime(worldAge, timeOfDay);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse time update packet", e);
		}

	}
}
