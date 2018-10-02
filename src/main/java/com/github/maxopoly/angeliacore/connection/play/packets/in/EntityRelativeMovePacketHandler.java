package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.EntityMoveEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.location.Location;

public class EntityRelativeMovePacketHandler extends AbstractIncomingPacketHandler {

    public EntityRelativeMovePacketHandler(ServerConnection connection) {
        super(connection, 0x26);
    }

    @Override
    public void handlePacket(ReadOnlyPacket packet) {
        try {
            int entityId = packet.readVarInt();
            short deltaX = packet.readShort();
            short deltaY = packet.readShort();
            short deltaZ = packet.readShort();
            boolean grounded = packet.readBoolean();
            LivingEntity entity = connection.getLivingEntityManager().getEntity(entityId);
            Location location = entity.getLocation();
            int currentX = location.getBlockX();
            int currentY = location.getBlockY();
            int currentZ = location.getBlockZ();
            entity.updatePosition(
                currentX + deltaX,
                currentY + deltaY,
                currentZ + deltaZ
            );
            entity.setGrounded(grounded);
            connection.getEventHandler().broadcast(new EntityMoveEvent(entity));
        }
        catch (EndOfPacketException e) {
            connection.getLogger().error("Failed to parse EntityRelativeMovePacket", e);
        }
    }

}