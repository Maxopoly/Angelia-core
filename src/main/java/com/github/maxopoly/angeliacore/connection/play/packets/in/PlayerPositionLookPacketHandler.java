package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionAndLookPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.TeleportConfirmPacket;
import com.github.maxopoly.angeliacore.event.events.TeleportByServerEvent;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;
import java.io.IOException;

public class PlayerPositionLookPacketHandler extends AbstractIncomingPacketHandler {

	public PlayerPositionLookPacketHandler(ServerConnection connection) {
		super(connection, 0x2F);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			// Extract data
			double x = packet.readDouble();
			double y = packet.readDouble();
			double z = packet.readDouble();
			float yaw = packet.readFloat();
			float pitch = packet.readFloat();
			byte flags = packet.readByte();
			int teleID = packet.readVarInt();
			// Extra data
			boolean xRelative = (flags & 0x01) != 0;
			boolean yRelative = (flags & 0x02) != 0;
			boolean zRelative = (flags & 0x04) != 0;
			boolean yawRelative = (flags & 0x08) != 0;
			boolean pitchRelative = (flags & 0x10) != 0;
			// Apply data
            DirectedLocation oldLocation = connection.getPlayerStatus().getLocation();
            DirectedLocation newLocation = new DirectedLocation(
                (xRelative) ? oldLocation.getX() + x : x,
                (yRelative) ? oldLocation.getY() + y : y,
                (zRelative) ? oldLocation.getZ() + z : z,
                (yawRelative) ? oldLocation.getYaw() + yaw : yaw,
                (pitchRelative) ? oldLocation.getPitch() + pitch : pitch
            );
            connection.getEventHandler().broadcast(new TeleportByServerEvent(oldLocation, newLocation));
            connection.getPlayerStatus().updateLocation(newLocation);
            connection.getPlayerStatus().updateLookingDirection(newLocation.getPitch(), newLocation.getYaw());
			connection.sendPacket(new TeleportConfirmPacket(teleID));
			connection.sendPacket(new PlayerPositionAndLookPacket(x, y, z, yaw, pitch, true));
			connection.getPlayerStatus().setInitialized();
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse PlayerPositionAndLookPacket", e);
		} catch (IOException e) {
			connection.getLogger().error("Failed to reply with Teleport ConfirmationPacket", e);
		}

	}

}
