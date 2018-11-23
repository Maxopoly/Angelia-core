package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.location.Location;

public class BlockBreakAnimationPacketHandler extends AbstractIncomingPacketHandler {

	public BlockBreakAnimationPacketHandler(ServerConnection connection) {
		super(connection, 0x08);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int entityID = packet.readVarInt();
			Location blockPos = packet.readPosition();
			byte stage = packet.readByte();
			//TODO?
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse break animation packet", e);
		}
	}

}
