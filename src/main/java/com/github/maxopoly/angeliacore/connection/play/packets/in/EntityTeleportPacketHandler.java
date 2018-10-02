package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.EntityMoveEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

public class EntityTeleportPacketHandler extends AbstractIncomingPacketHandler {

    public EntityTeleportPacketHandler(ServerConnection connection) {
        super(connection, 0x4C);
    }

    @Override
    public void handlePacket(ReadOnlyPacket packet) {
        try {
            // Get data from packet
            int entityId = packet.readVarInt();
            double x = packet.readDouble();
            double y = packet.readDouble();
            double z = packet.readDouble();
            float yaw = packet.readByte();
            float pitch = packet.readByte();
            boolean grounded = packet.readBoolean();
            // Apply data
            LivingEntity entity = connection.getLivingEntityManager().getEntity(entityId);
            entity.updatePosition(x, y, z);
            entity.updateLookingDirection(yaw, pitch);
            entity.setGrounded(grounded);
            connection.getEventHandler().broadcast(new EntityMoveEvent(entity));
        }
        catch (EndOfPacketException e) {
            connection.getLogger().error("Failed to parse EntityTeleportPacket", e);
        }
    }

}
