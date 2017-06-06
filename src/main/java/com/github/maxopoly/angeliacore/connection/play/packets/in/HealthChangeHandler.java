package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

public class HealthChangeHandler extends AbstractIncomingPacketHandler {

	public HealthChangeHandler(ServerConnection connection) {
		super(connection, 0x3E);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			float health = packet.readFloat();
			int food = packet.readVarInt();
			float foodSaturation = packet.readFloat();
			connection.getPlayerStatus().updateHealth(health, food, foodSaturation);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse health update packet", e);
		}
	}
}
