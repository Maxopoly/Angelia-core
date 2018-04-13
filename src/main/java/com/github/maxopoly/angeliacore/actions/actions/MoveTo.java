package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.actions.SequentialActionExecution;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionPacket;
import com.github.maxopoly.angeliacore.model.PlayerStatus;
import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.model.location.Location;
import java.io.IOException;

public class MoveTo extends AbstractAction {

	private Location destination;
	private double movementSpeed;
	private double ticksPerSecond;
	private final static double errorMargin = 0.01D;
	public static final double SLOW_SPEED = 1.0D;
	public static final double WALKING_SPEED = 4.317D;
	public static final double SPRINTING_SPEED = 5.612D;
	private static final double WALKING_LIMIT_TICK = 0.215D;
	private static final double SPRINTING_LIMIT_TICK = 0.280D;
	//public static final double WALKING_SPEED = 4.27;
	//public static final double SPRINTING_SPEED = 5.551;
	public static final double FALLING = 20.0;

	public MoveTo(ServerConnection connection, Location desto, double movementSpeed) {
		super(connection);
		this.destination = desto;
		this.movementSpeed = movementSpeed;
	}
	
	private long lastLog;

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
		double absDeltaX = Math.abs(deltaX);
		double absDeltaZ = Math.abs(deltaZ);
		if (movementSpeed == WALKING_SPEED) {
			if (absDeltaX > WALKING_LIMIT_TICK) {
				absDeltaX = WALKING_LIMIT_TICK;
			}
			if (absDeltaZ > WALKING_LIMIT_TICK) {
				absDeltaZ = WALKING_LIMIT_TICK;
			}
		} else if (movementSpeed == SPRINTING_SPEED) {
			if (absDeltaX > SPRINTING_LIMIT_TICK) {
				absDeltaX = SPRINTING_LIMIT_TICK;
			}
			if (absDeltaZ > SPRINTING_LIMIT_TICK) {
				absDeltaZ = SPRINTING_LIMIT_TICK;
			}
		}
		int dXs = sign(deltaX);
		int dZs = sign(deltaZ);
		
		double deltaXToUse = absDeltaX * dXs;
		double deltaZToUse = absDeltaZ * dZs;

		if ((System.currentTimeMillis() - lastLog) > 1000) {
			//connection.getLogger().info("DeltaX: " + Double.toString(deltaXToUse));
			//connection.getLogger().info("DeltaZ: " + Double.toString(deltaZToUse));
			lastLog = System.currentTimeMillis();
		}
		
		return new Location(current.getX() + deltaXToUse, current.getY(),
				current.getZ() + deltaZToUse, current.getYaw(), current.getPitch());
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
			boolean onGround = !connection.getPlayerStatus().isMidAir();
			//connection.getLogger().info("OnGround: " + onGround);
			connection.sendPacket(new PlayerPositionPacket(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), onGround));
		} catch (IOException e) {
			connection.getLogger().error("Failed to update location", e);
		}
	}

	@Override
	public void execute() {
		this.ticksPerSecond = connection.getTicksPerSecond();
		PlayerStatus status = connection.getPlayerStatus();
		status.updateLocation(getNextLocation(status.getLocation(), movementSpeed, ticksPerSecond));
		sendLocationPacket();
		if (isDone()) {
			connection.getPlayerStatus().setMidAir(false);
		}
	}

	@Override
	public boolean isDone() {
		return hasReachedDesto(connection.getPlayerStatus().getLocation());
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.LOOKING_DIRECTION, ActionLock.MOVEMENT };
	}
	
	private static int sign(double n) {
		return n > 0 ? 1 : n < 0 ? -1 : 0;
	}

}
