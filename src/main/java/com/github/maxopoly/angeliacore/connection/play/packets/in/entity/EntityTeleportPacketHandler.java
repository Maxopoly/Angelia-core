package com.github.maxopoly.angeliacore.connection.play.packets.in.entity;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.AbstractIncomingPacketHandler;
import com.github.maxopoly.angeliacore.event.events.entity.EntityTeleportEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;

public class EntityTeleportPacketHandler extends AbstractIncomingPacketHandler {

    public EntityTeleportPacketHandler(ServerConnection connection) {
        super(connection, 0x4C);
    }

    @Override
    public void handlePacket(ReadOnlyPacket packet) {
        try {
            int entityId = packet.readVarInt();
            double x = packet.readDouble();
            double y = packet.readDouble();
            double z = packet.readDouble();
            float yaw = packet.readByte();
            float pitch = packet.readByte();
            boolean grounded = packet.readBoolean();
            LivingEntity entity = connection.getEntityManager().getLivingEntity(entityId);
			if (entity == null) {
				return;
			}
            DirectedLocation newLoc = new DirectedLocation(x, y, z, yaw, pitch);
            connection.getEventHandler().broadcast(new EntityTeleportEvent(entity, entity.getLocation(), newLoc));
            entity.updateLocation(newLoc);
            entity.updateLookingDirection(yaw, pitch);
            entity.setOnGround(grounded);
        }
        catch (EndOfPacketException e) {
            connection.getLogger().error("Failed to parse EntityTeleportPacket", e);
        }
    }

}
