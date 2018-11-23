package com.github.maxopoly.angeliacore.connection.play.packets.in.entity;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.AbstractIncomingPacketHandler;
import com.github.maxopoly.angeliacore.event.events.entity.EntityLookEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;

public class EntityLookPacketHandler extends AbstractIncomingPacketHandler {

	public EntityLookPacketHandler(ServerConnection connection) {
		super(connection, 0x28);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int entityId = packet.readVarInt();
			float yaw = packet.readByte();
			float pitch = packet.readByte();
			boolean grounded = packet.readBoolean();
			LivingEntity entity = connection.getEntityManager().getLivingEntity(entityId);
			if (entity == null) {
				return;
			}
			DirectedLocation updatedLoc = new DirectedLocation(entity.getLocation(), yaw, pitch); 
			connection.getEventHandler().broadcast(new EntityLookEvent(entity, entity.getLocation(), updatedLoc));
			entity.updateLookingDirection(yaw, pitch);
			entity.setOnGround(grounded);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse EntityLookPacket", e);
		}
	}

}
