package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;

public class XPChangeHandler extends AbstractIncomingPacketHandler {

	public XPChangeHandler(ServerConnection connection) {
		super(connection, 0x3D);
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
