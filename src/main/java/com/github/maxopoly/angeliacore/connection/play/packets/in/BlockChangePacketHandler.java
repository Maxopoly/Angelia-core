package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.block.Block;
import com.github.maxopoly.angeliacore.block.BlockState;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.BlockChangeEvent;
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
            // Get chunk data
            Block oldBlock = connection.getChunkHolder().getBlock(x, y, z);
            Block newBlock = new Block(
                BlockState.getStateByData(data),
                new Location(x, y, z));
            connection.getChunkHolder().setBlock(newBlock);
            connection.getEventHandler().broadcast(new BlockChangeEvent(oldBlock, newBlock));
        }
        catch (EndOfPacketException e) {
            connection.getLogger().error("Failed to parse BlockChangePacket", e);
        }
    }

}
