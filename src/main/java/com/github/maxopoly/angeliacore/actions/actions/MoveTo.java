package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionPacket;
import com.github.maxopoly.angeliacore.model.ThePlayer;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;
import com.github.maxopoly.angeliacore.model.location.Location;

public class MoveTo extends AbstractAction {

	private Location destination;
	private double movementSpeed;
	private double ticksPerSecond;
	private final static double errorMargin = 0.0001D;
	public static final double SLOW_SPEED = 1.0D;
	public static final double WALKING_SPEED = 4.317D;
	public static final double SPRINTING_SPEED = 5.612D;
	public static final double FALLING = 20.0;

	public MoveTo(ServerConnection connection, Location desto, double movementSpeed) {
		super(connection);
		this.destination = desto;
		this.movementSpeed = movementSpeed;
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
	public DirectedLocation getNextLocation(DirectedLocation current, double movementSpeed, double ticksPerSecond) {
		double xDiff = destination.getX() - current.getX();
		//double yDiff = destination.getY() - current.getY();
		double zDiff = destination.getZ() - current.getZ();
		//connection.getPlayerStatus().setMidAir(yDiff > errorMargin);
		double distance = Math.sqrt((xDiff * xDiff) + (zDiff * zDiff));
		double timeTakenSeconds = distance / movementSpeed;
		double timeTakenTicks = timeTakenSeconds * ticksPerSecond;
		if (timeTakenTicks < 1.0) {
			timeTakenTicks = 1.0;
		}
		double deltaX = (xDiff / timeTakenTicks);
		double deltaZ = (zDiff / timeTakenTicks);
		return new DirectedLocation(current.getX() + deltaX, current.getY(),
				current.getZ() + deltaZ, current.getYaw(), current.getPitch());
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
			boolean onGround = connection.getPlayerStatus().isOnGround();
			connection.sendPacket(new PlayerPositionPacket(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), onGround));
		} catch (IOException e) {
			connection.getLogger().error("Failed to update location", e);
		}
	}

	@Override
	public void execute() {
		this.ticksPerSecond = connection.getTicksPerSecond();
		ThePlayer status = connection.getPlayerStatus();
		status.updateLocation(getNextLocation(status.getLocation(), movementSpeed, ticksPerSecond));
		sendLocationPacket();
		if (isDone()) {
			connection.getPlayerStatus().setOnGround(true);
		}
	}

	@Override
	public boolean isDone() {
		return hasReachedDesto(connection.getPlayerStatus().getLocation());
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.MOVEMENT };
	}
}
