package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.location.Location;

public class BlockChangePacketHandler extends AbstractIncomingPacketHandler {

	public BlockChangePacketHandler(ServerConnection connection) {
		super(connection, 0x0B);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			Location position = packet.readPosition();
			int data = packet.readVarInt();
			int x = position.getBlockX();
			int y = position.getBlockY();
			int z = position.getBlockZ();
			// TODO
			/*
			 * connection.getChunkHolder().setBlock(newBlock);
			 * connection.getEventHandler().broadcast(new BlockChangeEvent(oldBlock,
			 * newBlock));
			 */
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse BlockChangePacket", e);
		}
	}

}
