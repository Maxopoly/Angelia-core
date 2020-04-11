package com.github.maxopoly.angeliacore.connection.play.packets.in.entity;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.AbstractIncomingPacketHandler;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

public class EntityPacketHandler extends AbstractIncomingPacketHandler {

	public EntityPacketHandler(ServerConnection connection) {
		super(connection, 0x25);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		@SuppressWarnings("unused")
		int entityId = packet.readVarInt();
		// this one is weird
		// TODO investigate
		// See https://wiki.vg/index.php?title=Protocol&oldid=14204#Entity
		// This sort of means that the entity still exists but no actions were performed
		// on it.
		// I think it's safe to ignore this packet for now, as such, I have added a
		// @SuppressWarnings
	}

}
