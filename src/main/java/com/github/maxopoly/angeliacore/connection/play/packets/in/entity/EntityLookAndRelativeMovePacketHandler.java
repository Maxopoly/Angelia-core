package com.github.maxopoly.angeliacore.connection.play.packets.in.entity;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.in.AbstractIncomingPacketHandler;
import com.github.maxopoly.angeliacore.event.events.entity.EntityLookEvent;
import com.github.maxopoly.angeliacore.event.events.entity.EntityMoveEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;

public class EntityLookAndRelativeMovePacketHandler extends AbstractIncomingPacketHandler {

	public EntityLookAndRelativeMovePacketHandler(ServerConnection connection) {
		super(connection, 0x27);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int entityId = packet.readVarInt();
			short deltaX = packet.readShort();
			short deltaY = packet.readShort();
			short deltaZ = packet.readShort();
			float yaw = packet.readByte();
			float pitch = packet.readByte();
			boolean grounded = packet.readBoolean();
			LivingEntity entity = connection.getEntityManager().getLivingEntity(entityId);
			if (entity == null) {
				return;
			}
			DirectedLocation location = entity.getLocation();
			int currentX = location.getBlockX();
			int currentY = location.getBlockY();
			int currentZ = location.getBlockZ();
			DirectedLocation updatedLoc = new DirectedLocation(currentX + deltaX, currentY + deltaY, currentZ + deltaZ, yaw, pitch);
			connection.getEventHandler().broadcast(new EntityMoveEvent(entity, entity.getLocation(), updatedLoc));
			connection.getEventHandler().broadcast(new EntityLookEvent(entity, entity.getLocation(), updatedLoc));			
			entity.updateLocation(updatedLoc);
			entity.updateLookingDirection(yaw, pitch);
			entity.setOnGround(grounded);
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse EntityLookAndRelativeMovePacket", e);
		}
	}

}