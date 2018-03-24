package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.PlayerSpawnEvent;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.packet.EndOfPacketException;
import com.github.maxopoly.angeliacore.packet.ReadOnlyPacket;
import java.util.UUID;

public class SpawnPlayerPacketHandler extends AbstractIncomingPacketHandler {

	public SpawnPlayerPacketHandler(ServerConnection connection) {
		super(connection, 0x05);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			int entityID = packet.readVarInt();
			UUID uuid = packet.readUUID();
			double x = packet.readDouble();
			double y = packet.readDouble();
			double z = packet.readDouble();
			byte yaw = packet.readUnsignedByte();
			byte pitch = packet.readUnsignedByte();
			// TODO turn this into a proper entity and shit
			connection.getEventHandler().broadcast(
					new PlayerSpawnEvent(new Location(x, y, z, Location.translateAngleFrom256Step(yaw), Location
							.translateAngleFrom256Step(pitch)), connection.getOtherPlayerManager().getPlayer(uuid),
							entityID));
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse player spawn packet", e);
		}

	}

}
