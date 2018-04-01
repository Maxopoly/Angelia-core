package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

public class BlockBreakAnimationPacketHandler extends AbstractIncomingPacketHandler {

	public BlockBreakAnimationPacketHandler(ServerConnection connection) {
		super(connection, 0x08);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int entityID = packet.readVarInt();
			Location blockPos = packet.readPosition();
			byte stage = packet.readUnsignedByte();
			//TODO?
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse break animation packet", e);
		}
	}

}
