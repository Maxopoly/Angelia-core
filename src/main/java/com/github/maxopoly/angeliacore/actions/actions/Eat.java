package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BlockPlacementPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerDiggingPacket;
import com.github.maxopoly.angeliacore.model.BlockFace;
import com.github.maxopoly.angeliacore.model.Location;
import java.io.IOException;

/**
 * Assumes the currently selected slot is food and attempts to eat it
 *
 */
public class Eat extends AbstractAction {

	private int totalTicks;
	private int ticksLeftToWait;

	public Eat(ServerConnection connection, int ticksTaken) {
		super(connection);
		this.totalTicks = ticksTaken;
		this.ticksLeftToWait = ticksTaken;
	}

	@Override
	public void execute() {
		if (ticksLeftToWait == totalTicks) {
			try {
				BlockPlacementPacket eatingStarted = new BlockPlacementPacket(new Location(-1, -1, -1), BlockFace.SPECIAL);
				connection.sendPacket(eatingStarted);
			} catch (IOException e) {
				connection.getLogger().error("Failed to send eating start packet", e);
			}
		}
		if (ticksLeftToWait == 0) {
			try {
				PlayerDiggingPacket finishedEating = new PlayerDiggingPacket(5, new Location(0, 0, 0), BlockFace.SPECIAL);
				connection.sendPacket(finishedEating);
			} catch (IOException e) {
				connection.getLogger().error("Failed to send eating stop packet", e);
			}
		}
		ticksLeftToWait--;
	}

	@Override
	public boolean isDone() {
		return ticksLeftToWait <= -1;
	}
}
