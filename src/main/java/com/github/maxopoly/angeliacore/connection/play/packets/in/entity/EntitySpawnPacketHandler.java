package com.github.maxopoly.angeliacore.connection.play.packets.in.entity;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.AbstractIncomingPacketHandler;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;

public class EntitySpawnPacketHandler extends AbstractIncomingPacketHandler {

	public EntitySpawnPacketHandler(ServerConnection connection) {
		super(connection, 0x00);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {

	}

}
