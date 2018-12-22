package com.github.maxopoly.angeliacore.connection.play.packets.in;

import java.util.UUID;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.PlayerSpawnEvent;
import com.github.maxopoly.angeliacore.libs.packetEncoding.EndOfPacketException;
import com.github.maxopoly.angeliacore.libs.packetEncoding.ReadOnlyPacket;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;

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
			DirectedLocation location = new DirectedLocation(x, y, z, DirectedLocation.translateAngleFrom256Step(yaw),
					DirectedLocation.translateAngleFrom256Step(pitch));
			// TODO turn this into a proper entity and shit
			connection.getEventHandler().broadcast(
					new PlayerSpawnEvent(location, connection.getOtherPlayerManager().getPlayer(uuid), null));
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse player spawn packet", e);
		}

	}

}
