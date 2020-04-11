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
			int data = packet.readVarInt();
			BlockState oldState = position.getBlockAt(connection);
			BlockState newState = BlockStateFactory.getStateByData(data);
			 connection.getEventHandler().broadcast(new BlockChangeEvent(oldState,
					 newState));
			 connection.getChunkHolder().setBlock(position, newState);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse BlockChangePacket", e);
		}
	}

}
