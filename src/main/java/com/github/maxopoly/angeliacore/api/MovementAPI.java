package com.github.maxopoly.angeliacore.api;

import com.github.maxopoly.angeliacore.model.Location;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionAndLookPacket;
import java.io.IOException;

public class MovementAPI {

	private ServerConnection connection;

	public MovementAPI(ServerConnection connection) {
		this.connection = connection;
	}

	public void move(MovementDirection direction) {
		Location afterLocation = applyTo(connection.getPlayerStatus().getLocation(), direction, 1.0);
		connection.getPlayerStatus().updatePosition(afterLocation.getX(), afterLocation.getY(), afterLocation.getZ());
		updateLocation();
	}

	/**
	 * Updates the player location server side by sending a packet containing the player location client side
	 */
	public void updateLocation() {
		Location playerLoc = connection.getPlayerStatus().getLocation();
		try {
			connection.sendPacket(new PlayerPositionAndLookPacket(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(),
					playerLoc.getYaw(), playerLoc.getPitch(), true));
		} catch (IOException e) {
			connection.getLogger().error("Failed to update location", e);
		}
	}

	private static Location applyTo(Location loc, MovementDirection direction, double distance) {
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		switch (direction) {
			case NORTH:
				z += distance;
				break;
			case SOUTH:
				z -= distance;
				break;
			case EAST:
				x += distance;
				break;
			case WEST:
				x -= distance;
				break;
			case NORTH_WEST:
				z += distance;
				x -= distance;
				break;
			case NORTH_EAST:
				z += distance;
				x += distance;
				break;
			case SOUTH_WEST:
				z -= distance;
				x += distance;
				break;
			case SOUTH_EAST:
				z -= distance;
				x -= distance;
				break;
			case UP:
				y += distance;
				break;
			case DOWN:
				y -= distance;
				break;
			case NORTH_UP:
				z += distance;
				y += distance;
				break;
			case SOUTH_UP:
				z -= distance;
				y += distance;
				break;
			case EAST_UP:
				x += distance;
				y += distance;
				break;
			case WEST_UP:
				x -= distance;
				y += distance;
				break;
			case NORTH_WEST_UP:
				z += distance;
				x -= distance;
				y += distance;
				break;
			case NORTH_EAST_UP:
				z += distance;
				x += distance;
				y += distance;
				break;
			case SOUTH_WEST_UP:
				z -= distance;
				x += distance;
				y += distance;
				break;
			case SOUTH_EAST_UP:
				z -= distance;
				x -= distance;
				y += distance;
			case NORTH_DOWN:
				z += distance;
				y -= distance;
				break;
			case SOUTH_DOWN:
				z -= distance;
				y -= distance;
				break;
			case EAST_DOWN:
				x += distance;
				y -= distance;
				break;
			case WEST_DOWN:
				x -= distance;
				y -= distance;
				break;
			case NORTH_WEST_DOWN:
				z += distance;
				x -= distance;
				y -= distance;
				break;
			case NORTH_EAST_DOWN:
				z += distance;
				x += distance;
				y -= distance;
				break;
			case SOUTH_WEST_DOWN:
				z -= distance;
				x += distance;
				y -= distance;
				break;
			case SOUTH_EAST_DOWN:
				z -= distance;
				x -= distance;
				y -= distance;
		}
		return new Location(x, y, z, loc.getYaw(), loc.getPitch());
	}
}
