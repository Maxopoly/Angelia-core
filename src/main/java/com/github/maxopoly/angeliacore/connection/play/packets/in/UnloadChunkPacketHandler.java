package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.ChunkUnloadEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.block.Chunk;

public class UnloadChunkPacketHandler extends AbstractIncomingPacketHandler {

	public UnloadChunkPacketHandler(ServerConnection connection) {
		super(connection, 0x1D);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		if (!connection.getChunkHolder().isHoldingModel()) {
			return;
		}
		try {
			int x = packet.readInt();
			int z = packet.readInt();
			Chunk chunk = connection.getChunkHolder().getChunk(x, z);
			connection.getEventHandler().broadcast(new ChunkUnloadEvent(chunk));
			connection.getChunkHolder().removeChunk(x, z);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse break animation packet", e);
		}
	}

}