package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionAndLookPacket;
import com.github.maxopoly.angeliacore.model.Location;
import com.github.maxopoly.angeliacore.model.PlayerStatus;
import java.io.IOException;

public class MovementAction extends AbstractAction {

	private Location destination;
	private double movementSpeed;
	private double ticksPerSecond;
	private final static double errorMargin = 0.001;
	public static final double SPRINTING_SPEED = 5.4;

	public MovementAction(ServerConnection connection, Location desto, double movementSpeed, double ticksPerSecond) {
		super(connection);
		this.destination = desto;
		this.movementSpeed = movementSpeed;
		this.ticksPerSecond = ticksPerSecond;
	}

	/**
	 * Calculates the location to which the player should move on the next client tick
	 * 
	 * @param current
	 *          The players current location
	 * @param movementSpeed
	 *          The speed at which the player is supposed to move
	 * @return
	 */
	public Location getNextLocation(Location current, double movementSpeed, double ticksPerSecond) {
		double xDiff = destination.getX() - current.getX();
		double zDiff = destination.getZ() - current.getZ();
		// TODO y?
		double distance = Math.sqrt((xDiff * xDiff) + (zDiff * zDiff));
		double timeTakenSeconds = distance / movementSpeed;
		double timeTakenTicks = timeTakenSeconds * ticksPerSecond;
		return new Location(current.getX() + (xDiff / timeTakenTicks), current.getY(), current.getZ()
				+ (zDiff / timeTakenTicks), current.getYaw(), current.getPitch());
	}

	public boolean hasReachedDesto(Location current) {
		return Math.abs(current.getX() - destination.getX()) < errorMargin
				&& Math.abs(current.getZ() - destination.getZ()) < errorMargin;
	}

	public Location getDestination() {
		return destination;
	}

	public void sendLocationPacket() {
		Location playerLoc = connection.getPlayerStatus().getLocation();
		try {
			connection.sendPacket(new PlayerPositionAndLookPacket(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(),
					playerLoc.getYaw(), playerLoc.getPitch(), true));
		} catch (IOException e) {
			connection.getLogger().error("Failed to update location", e);
		}
	}

	@Override
	public void execute() {
		PlayerStatus status = connection.getPlayerStatus();
		status.updateLocation(getNextLocation(status.getLocation(), movementSpeed, ticksPerSecond));
		sendLocationPacket();
	}

	@Override
	public boolean isDone() {
		return hasReachedDesto(connection.getPlayerStatus().getLocation());
	}

}
