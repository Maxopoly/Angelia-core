package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

public class PlayerRespawnPacketHandler extends AbstractIncomingPacketHandler {

	public PlayerRespawnPacketHandler(ServerConnection connection) {
		super(connection, 0x35);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int dimension = packet.readInt();
			byte difficulty = packet.readByte();
			byte gameMode = packet.readByte();
			String lvlType = packet.readString();
			connection.getWorldStatusHandler().updateOnRespawn(dimension, difficulty, lvlType);
			connection.getPlayerStatus().setDead(false);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse set slot packet", e);
		}

	}

}
