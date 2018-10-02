package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.EntityLoadEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

public class EntityPacketHandler extends AbstractIncomingPacketHandler {

    public EntityPacketHandler(ServerConnection connection) {
        super(connection, 0x25);
    }

    @Override
    public void handlePacket(ReadOnlyPacket packet) {
        int entityId = packet.readVarInt();
        LivingEntity entity = new LivingEntity(entityId);
        connection.getLivingEntityManager().addEntity(entity);
        connection.getEventHandler().broadcast(new EntityLoadEvent(entity));
    }

}
