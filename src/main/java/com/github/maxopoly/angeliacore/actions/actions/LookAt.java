package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerPositionAndLookPacket;
import com.github.maxopoly.angeliacore.model.ThePlayer;
import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;
import com.github.maxopoly.angeliacore.model.location.Location;
import java.io.IOException;

public class LookAt extends AbstractAction {

	private Location target;
	private int totalTicksToTake;
	private int ticksLeft;
	private float yawPerTick;
	private float pitchPerTick;

	/**
	 * Instantly changes the location the player looks at
	 *
	 * @param offset
	 *          The block the player should be looking at
	 */
	public LookAt(ServerConnection connection, Location offset) {
		this(connection, offset, 1);
	}

	/**
	 * Looks at a specific side of the given block
	 *
	 * @param connection
	 *          ServerConnection
	 * @param block
	 *          Block to look at
	 * @param face
	 *          Relative side to look at
	 */
	public LookAt(ServerConnection connection, Location block, BlockFace face) {
		this(connection, new Location(block.getBlockCenter().add(face.toVector().multiply(0.5))));
	}

	/**
	 * Turns the players head, while taking the given amount of time
	 *
	 * @param offSet
	 *          Block to look at
	 * @param ticksToTake
	 *          Time that should be taken to turn the head
	 */
	public LookAt(ServerConnection connection, Location offSet, int ticksToTake) {
		super(connection);
		this.target = offSet;
		this.totalTicksToTake = ticksToTake;
		this.ticksLeft = ticksToTake;
		DirectedLocation loc = connection.getPlayerStatus().getHeadLocation();
		double deltaX = loc.getX() - offSet.getX();
		double deltaY = loc.getY() - offSet.getY();
		double deltaZ = loc.getZ() - offSet.getZ();
		double radius = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
		deltaX /= radius;
		deltaY /= radius;
		deltaZ /= radius;
		double yaw = Math.atan2(deltaX, -deltaZ) / Math.PI * 180.0;
		double pitch = Math.asin(deltaY) / Math.PI * 180.0;
		if (yaw < -180) {
			yaw += 360;
		}
		if (yaw > 180) {
			yaw -= 360;
		}
		yawPerTick = (float) (yaw - loc.getYaw()) / ticksToTake;
		pitchPerTick = (float) (pitch - loc.getPitch()) / ticksToTake;
	}

	@Override
	public void execute() {
		if (isDone()) {
			return;
		}
		ThePlayer status = connection.getPlayerStatus();
		status.updateLookingDirection(status.getLocation().getYaw() + yawPerTick, status.getLocation().getPitch()
				+ pitchPerTick);
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

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.LOOKING_DIRECTION };
	}

}
