package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public class XPChangeHandler extends AbstractIncomingPacketHandler {

	public XPChangeHandler(ServerConnection connection) {
		super(connection, 0x40);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			float progress = packet.readFloat();
			int level = packet.readVarInt();
			int totalXP = packet.readVarInt();
			connection.getPlayerStatus().updateXP(progress, level, totalXP);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse XP update packet", e);
		}
	}

}
