package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionPacket;
import com.github.maxopoly.angeliacore.model.ThePlayer;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;

public class MoveRelative extends AbstractAction {

	private int remainingTicks;
	private Vector direction;
	private double speed;

	public MoveRelative(ServerConnection connection, Vector direction, int ticks, double speed) {
		super(connection);
		this.remainingTicks = ticks;
		this.direction = new Vector(direction.getX(), 0, direction.getZ());
		this.direction.normalize();
	}

	@Override
	public void execute() {
		double ticksPerSecond = connection.getTicksPerSecond();
		ThePlayer status = connection.getPlayerStatus();
		status.updateLocation(getNextLocation(status.getLocation(), speed, ticksPerSecond));
		sendLocationPacket();
		if (isDone()) {
			connection.getPlayerStatus().setOnGround(true);
		}
	}

	/**
	 * Get the next location in the chain for movement
	 * 
	 * @param current        - The current location
	 * @param movementSpeed  - The movement speed
	 * @param ticksPerSecond - The TPS of the server
	 * @return The next location in the movement chain
	 */
	public DirectedLocation getNextLocation(DirectedLocation current, double movementSpeed, double ticksPerSecond) {
		double movementPerTick = movementSpeed / ticksPerSecond;
		double xDiff = direction.getX() * movementPerTick;
		double zDiff = direction.getZ() * movementPerTick;
		return new DirectedLocation(current.getX() + xDiff, current.getY(), current.getZ() + zDiff, current.getYaw(),
				current.getPitch());
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.MOVEMENT };
	}

	@Override
	public boolean isDone() {
		return remainingTicks <= 0;
	}

	/**
	 * Sends the packet which handles the location
	 */
	public void sendLocationPacket() {
		Location playerLoc = connection.getPlayerStatus().getLocation();
		try {
			boolean onGround = connection.getPlayerStatus().isOnGround();
			connection.sendPacket(
					new PlayerPositionPacket(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), onGround));
		} catch (IOException e) {
			connection.getLogger().error("Failed to update location", e);
		}
	}

}
