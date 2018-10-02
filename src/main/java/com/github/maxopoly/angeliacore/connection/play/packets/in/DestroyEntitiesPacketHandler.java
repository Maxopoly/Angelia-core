package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.EntityUnloadEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

public class DestroyEntitiesPacketHandler extends AbstractIncomingPacketHandler {

    public DestroyEntitiesPacketHandler(ServerConnection connection) {
        super(connection, 0x32);
    }

    @Override
    public void handlePacket(ReadOnlyPacket packet) {
        int entitiesLength = packet.readVarInt();
        for (int i = 0; i < entitiesLength; i++) {
            int entityId = packet.readVarInt();
            LivingEntity entity = connection.getLivingEntityManager().getEntity(entityId);
            connection.getEventHandler().broadcast(new EntityUnloadEvent(entity));
            connection.getLivingEntityManager().removeEntity(entityId);
        }
    }

}
