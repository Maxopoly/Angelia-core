package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BreakAnimationPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerDiggingPacket;
import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.model.location.Location;
import java.io.IOException;

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
	}

	public Location getBlockLocation() {
		return blockLocation;
	}

	/**
	 * 
	 * @return Face the block is being broken from
	 */
	public BlockFace getFace() {
		return face;
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

	private void sendDiggingPacket(int status) {
		try {
			PlayerDiggingPacket packet = new PlayerDiggingPacket(status, blockLocation, face);
			connection.sendPacket(packet);
		} catch (IOException e) {
			connection.getLogger().error("Failed to send digging packet", e);
		}
	}

	private void sendBreakAnimation() {
		try {
			BreakAnimationPacket packet2 = new BreakAnimationPacket();
			connection.sendPacket(packet2);
		} catch (IOException e) {
			connection.getLogger().error("Failed to send digging animation packet", e);
		}
	}

	@Override
	public boolean isDone() {
		return remainingTicks < 0;
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.HOTBAR_SLOT };
	}

}
