package com.github.maxopoly.angeliacore.connection.play.packets.in;

import com.github.maxopoly.angeliacore.binary.EndOfPacketException;
import com.github.maxopoly.angeliacore.binary.ReadOnlyPacket;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionAndLookPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.TeleportConfirmPacket;
import com.github.maxopoly.angeliacore.event.events.TeleportByServerEvent;
import com.github.maxopoly.angeliacore.model.location.Location;
import java.io.IOException;

public class PlayerPositionLookPacketHandler extends AbstractIncomingPacketHandler {

	public PlayerPositionLookPacketHandler(ServerConnection connection) {
		super(connection, 0x2F);
	}

	@Override
	public void handlePacket(ReadOnlyPacket packet) {
		try {
			double x = packet.readDouble();
			double y = packet.readDouble();
			double z = packet.readDouble();
			float yaw = packet.readFloat();
			float pitch = packet.readFloat();
			byte flags = packet.readByte();
			Location status = connection.getPlayerStatus().getLocation();
			boolean xRelative = (flags & 0x01) != 0;
			if (xRelative) {
				x = status.getX() + x;
			}
			boolean yRelative = (flags & 0x02) != 0;
			if (yRelative) {
				y = status.getY() + y;
			}
			boolean zRelative = (flags & 0x04) != 0;
			if (zRelative) {
				z = status.getZ() + z;
			}
			boolean yawRelative = (flags & 0x08) != 0;
			if (yawRelative) {
				yaw = status.getYaw() + yaw;
			}
			boolean pitchRelative = (flags & 0x10) != 0;
			if (pitchRelative) {
				pitch = status.getPitch() + pitch;
			}
			connection.getPlayerStatus().updatePosition(x, y, z);
			connection.getPlayerStatus().updateLookingDirection(yaw, pitch);
			Location dictatedLocation = new Location(x, y, z, yaw, pitch);
			int teleID = packet.readVarInt();
			connection.sendPacket(new TeleportConfirmPacket(teleID));
			connection.sendPacket(new PlayerPositionAndLookPacket(x, y, z, yaw, pitch, true));
			connection.getEventHandler().broadcast(new TeleportByServerEvent(status, dictatedLocation));
		} catch (EndOfPacketException e) {
			connection.getLogger().error("Failed to parse PlayerPositionAndLookPacket", e);
		} catch (IOException e) {
			connection.getLogger().error("Failed to reply with Teleport ConfirmationPacket", e);
		}

	}

}
