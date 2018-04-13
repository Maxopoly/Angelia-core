package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public class JoinGamePacketHandler extends AbstractIncomingPacketHandler {

	public JoinGamePacketHandler(ServerConnection connection) {
		super(connection, 0x23);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int playerEntityID = packet.readInt();
			connection.getPlayerStatus().setEntityID(playerEntityID);
			byte gameMode = packet.readByte();
			int dimension = packet.readInt();
			byte difficulty = packet.readByte();
			byte maxPlayers = packet.readByte();
			String lvlType = packet.readString();
			boolean debugInfo = packet.readBoolean();
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse Join game packet", e);
		}

	}

}
