package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.world.BlockChangeEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.block.BlockStateFactory;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.location.Location;

public class BlockChangePacketHandler extends AbstractIncomingPacketHandler {

	public BlockChangePacketHandler(ServerConnection connection) {
		super(connection, 0x0B);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			Location position = packet.readPosition();
			BlockState oldBlock = position.getBlockAt(this.connection);
			int data = packet.readVarInt();

			BlockState newBlock = BlockStateFactory.getStateByData(data);

			connection.getChunkHolder().setBlock(position, newBlock);
			connection.getEventHandler().broadcast(new BlockChangeEvent(oldBlock, newBlock));

		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse BlockChangePacket", e);
		}
	}

}
