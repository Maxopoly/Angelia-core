package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.model.Location;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BreakAnimationPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerDiggingPacket;
import java.io.IOException;

public class BreakAction extends AbstractAction {

	private Location blockLocation;
	private int breakingTicksTotal;
	private int remainingTicks;
	private byte face;

	public BreakAction(ServerConnection connection, Location loc, int breakingTicks, byte face) {
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
	 * 0 = bottom, 1 = top, 2 = North, 3 = South, 4 = West, 5 = East, 255 = Special
	 * 
	 * @return Face the block is being broken from
	 */
	public byte getFace() {
		return face;
	}

	@Override
	public void execute() {
		int status = -1;
		remainingTicks--;
		if (isDone()) {
			// break it
			status = 2;
		} else if ((breakingTicksTotal - 1) == remainingTicks) {
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
	}

	@Override
	public boolean isDone() {
		return remainingTicks <= 0;
	}

}
