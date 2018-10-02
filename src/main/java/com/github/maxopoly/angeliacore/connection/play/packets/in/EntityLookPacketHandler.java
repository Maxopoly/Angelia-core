package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

public class EntityLookPacketHandler extends AbstractIncomingPacketHandler {

    public EntityLookPacketHandler(ServerConnection connection) {
        super(connection, 0x28);
    }

    @Override
    public void handlePacket(ReadOnlyPacket packet) {
        try {
            // Get data from packet
            int entityId = packet.readVarInt();
            float yaw = packet.readByte();
            float pitch = packet.readByte();
            boolean grounded = packet.readBoolean();
            // Apply data
            LivingEntity entity = connection.getLivingEntityManager().getEntity(entityId);
            entity.updateLookingDirection(yaw, pitch);
            entity.setGrounded(grounded);
        }
        catch (EndOfPacketException e) {
            connection.getLogger().error("Failed to parse EntityLookPacket", e);
        }
    }

}
