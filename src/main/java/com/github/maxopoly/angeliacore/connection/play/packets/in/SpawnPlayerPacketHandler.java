package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.PlayerSpawnEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.location.Location;
import java.util.UUID;

public class SpawnPlayerPacketHandler extends AbstractIncomingPacketHandler {

	public SpawnPlayerPacketHandler(ServerConnection connection) {
		super(connection, 0x05);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int entityId = packet.readVarInt();
			UUID uuid = packet.readUUID();
			double x = packet.readDouble();
			double y = packet.readDouble();
			double z = packet.readDouble();
			byte yaw = packet.readByte();
			byte pitch = packet.readByte();
            LivingEntity entity = connection.getLivingEntityManager().getEntity(entityId);
            Location location = new Location(x, y, z, Location.translateAngleFrom256Step(yaw), Location.translateAngleFrom256Step(pitch));
            entity.updateLocation(location);
			connection.getEventHandler().broadcast(new PlayerSpawnEvent(location, connection.getOtherPlayerManager().getPlayer(uuid), entity));
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse player spawn packet", e);
		}

	}

}
