package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.gamelogic.physics.PlayerPhysicsManager;
import com.github.maxopoly.angeliacore.model.ThePlayer;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;

public class MoveTo extends AbstractAction {

	protected Location destination;
	protected double movementSpeed;
	protected PlayerPhysicsManager physicsManager;

	public MoveTo(ServerConnection connection, Location desto, double movementSpeed) {
		super(connection);
		this.physicsManager = connection.getPhysicsManager();
		this.destination = desto;
		this.movementSpeed = movementSpeed;
	}

	@Override
	public void execute() {
		double movementPerTick = movementSpeed / connection.getTicksPerSecond();
		ThePlayer player = connection.getPlayerStatus();
		if (destination.removeComponent(1).subtract(player.getLocation().removeComponent(1))
				.getLength() < movementPerTick) {
			// Approaching a location by setting the acceleration is a differential equation
			// and complicated, so we just teleport once we are less than a tick away
			player.updateLocation(new Location(destination.getX(), player.getLocation().getY(), destination.getZ()));
		} else {
			physicsManager
					.addAcceleration(getAccelerationToApply(connection.getPlayerStatus().getLocation(), movementSpeed));
		}
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.MOVEMENT };
	}

	public Location getDestination() {
		return destination;
	}

	/**
	 * Calculates the acceleration to apply to the player for the next physics tick
	 *
	 * @param current        The players current location
	 * @param movementSpeed  The speed at which the player is supposed to move
	 * @param ticksPerSecond How many client ticks are in one second
	 * @return Acceleration to apply on next tick in m/s^2
	 */
	public Vector getAccelerationToApply(DirectedLocation current, double movementSpeed) {
		double xDiff = destination.getX() - current.getX();
		double zDiff = destination.getZ() - current.getZ();
		Vector relative = new Vector(xDiff, 0, zDiff);
		return relative.normalize().multiply(movementSpeed);
	}

	/**
	 * Checks if the player has reached the destination within an error margin. As
	 * of right now, it doesn't account for the Y coordinate.
	 * 
	 * @param current - The current location of the player
	 * @return - Whether the player has reached the destination
	 */
	public boolean hasReachedDesto(Location current) {
		return Math.abs(current.getX() - destination.getX()) < connection.getConfig().getPhysicsDelta()
				&& Math.abs(current.getZ() - destination.getZ()) < connection.getConfig().getPhysicsDelta();
	}

	@Override
	public boolean isDone() {
		return hasReachedDesto(connection.getPlayerStatus().getLocation());
	}
}
