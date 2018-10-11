package com.github.maxopoly.angeliacore.connection.play.packets.in.entity;

import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.AbstractIncomingPacketHandler;

public class EntityPacketHandler extends AbstractIncomingPacketHandler {

    public EntityPacketHandler(ServerConnection connection) {
        super(connection, 0x25);
    }

    @Override
    public void handlePacket(ReadOnlyPacket packet) {
        int entityId = packet.readVarInt();
        //this one is weird
        //TODO investigate
    }

}
