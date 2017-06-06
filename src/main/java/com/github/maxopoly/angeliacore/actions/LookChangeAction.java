package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.model.Location;

import com.github.maxopoly.angeliacore.model.PlayerStatus;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionAndLookPacket;
import java.io.IOException;

public class LookChangeAction extends AbstractAction {

	private Location target;
	private int totalTicksToTake;
	private int ticksLeft;
	private float yawPerTick;
	private float pitchPerTick;
	private static final double headElevation = 1.62; // how far the player head is above the y location

	/**
	 * Instantly changes the location the player looks at
	 * 
	 * @param offset
	 *          The block the player should be looking at
	 */
	public LookChangeAction(ServerConnection connection, Location offset) {
		this(connection, offset, 1);
	}

	/**
	 * Turns the players head, while taking the given amount of time
	 * 
	 * @param offSet
	 *          Block to look at
	 * @param ticksToTake
	 *          Time that should be taken to turn the head
	 */
	public LookChangeAction(ServerConnection connection, Location offSet, int ticksToTake) {
		super(connection);
		this.target = offSet;
		this.totalTicksToTake = ticksToTake;
		this.ticksLeft = ticksToTake;
		Location loc = connection.getPlayerStatus().getHeadLocation();
		System.out.println(loc.toString());
		double deltaX = loc.getX() - offSet.getX();
		double deltaY = loc.getY() - offSet.getY();
		double deltaZ = loc.getZ() - offSet.getZ();
		double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
		deltaX /= radius;
		deltaY /= radius;
		deltaZ /= radius;
		double yaw = Math.atan2((-1) * deltaZ, (-1) * deltaX) / Math.PI * 180.0;
		double pitch = Math.asin(deltaY) / Math.PI * 180.0;
		yaw -= 90;
		if (yaw < -180) {
			yaw += 360;
		}
		if (yaw > 180) {
			yaw -= 360;
		}
		System.out.println(offSet.toString());
		System.out.println(yaw + " " + pitch);
		yawPerTick = (float) (yaw - loc.getYaw()) / ticksToTake;
		pitchPerTick = (float) (pitch - loc.getPitch()) / ticksToTake;

	}

	@Override
	public void execute() {
		if (isDone()) {
			return;
		}
		PlayerStatus status = connection.getPlayerStatus();
		status.updateLookingDirection(status.getLocation().getYaw() + yawPerTick, status.getLocation().getPitch()
				+ pitchPerTick);
		System.out.println(status.getLocationString());
		try {
			PlayerPositionAndLookPacket packet = new PlayerPositionAndLookPacket(status.getLocation(), true);
			connection.sendPacket(packet);
		} catch (IOException e) {
			connection.getLogger().error("Failed to update looking direction", e);
		}
		ticksLeft--;
	}

	@Override
	public boolean isDone() {
		return ticksLeft <= 0;
	}

}
