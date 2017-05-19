package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

public class JoinGamePacketHandler extends AbstractIncomingPacketHandler {

	public JoinGamePacketHandler(ServerConnection connection) {
		super(connection, 0x23);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int playerEntityID = packet.readSignedInt();
			byte gameMode = packet.readUnsignedByte();
			int dimension = packet.readSignedInt();
			byte difficulty = packet.readUnsignedByte();
			byte maxPlayers = packet.readUnsignedByte();
			String lvlType = packet.readString();
			boolean debugInfo = packet.readBoolean();
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse Join game packet", e);
		}

	}

}
