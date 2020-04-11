package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BreakAnimationPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerDiggingPacket;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.model.location.Location;

public class BreakBlock extends AbstractAction {

	private Location blockLocation;
	private int breakingTicksTotal;
	private int remainingTicks;
	private BlockFace face;

	public BreakBlock(ServerConnection connection, Location loc, int breakingTicks, BlockFace face) {
		super(connection);
		this.blockLocation = loc;
		this.breakingTicksTotal = breakingTicks;
		this.remainingTicks = breakingTicks;
		this.face = face;
		this.connection = connection;
	}

	@Override
	public void execute() {
		if (breakingTicksTotal == remainingTicks) {
			// just started
			sendDiggingPacket(0);
		}
		if (remainingTicks == 0) {
			// done, so we break it
			sendDiggingPacket(2);
		}
		sendBreakAnimation();
		remainingTicks--;
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.HOTBAR_SLOT };
	}

	public Location getBlockLocation() {
		return blockLocation;
	}

	/**
	 * @return Face the block is being broken from
	 */
	public BlockFace getFace() {
		return face;
	}

	/**
	 * @return The block being broken
	 */
	public BlockState getBlock() {
		return getBlockLocation().getBlockAt(connection);
	}

	@Override
	public boolean isDone() {
		return remainingTicks < 0;
	}

	/**
	 * Send the break animation for the block
	 */
	private void sendBreakAnimation() {
		try {
			BreakAnimationPacket packet2 = new BreakAnimationPacket();
			connection.sendPacket(packet2);
		} catch (IOException e) {
			connection.getLogger().error("Failed to send digging animation packet", e);
		}
	}

	/**
	 * Send the actual digging packet
	 * 
	 * @param status The current status of the digging
	 */
	private void sendDiggingPacket(int status) {
		try {
			PlayerDiggingPacket packet = new PlayerDiggingPacket(status, blockLocation, face);
			connection.sendPacket(packet);
		} catch (IOException e) {
			connection.getLogger().error("Failed to send digging packet", e);
		}
	}

}
