package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BreakAnimationPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerDiggingPacket;
import com.github.maxopoly.angeliacore.model.BlockFace;
import com.github.maxopoly.angeliacore.model.Location;
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
		int status = -1;
		if (remainingTicks == 0) {
			// break it
			status = 2;
		} else if ((breakingTicksTotal) == remainingTicks) {
			// started digging
			status = 0;
		} else {
			// nothing is actually done if we are just in the middle of breaking
			status = -1;
		}
		try {
			if (status != -1) {
				PlayerDiggingPacket packet = new PlayerDiggingPacket(status, blockLocation, face);
				connection.sendPacket(packet);
			}
			BreakAnimationPacket packet2 = new BreakAnimationPacket();
			connection.sendPacket(packet2);
		} catch (IOException e) {
			connection.getLogger().error("Failed to send digging packet", e);
		}
		remainingTicks--;
	}

	@Override
	public boolean isDone() {
		return remainingTicks < 0;
	}

}
