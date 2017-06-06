package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BlockPlacementPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.PlayerDiggingPacket;
import com.github.maxopoly.angeliacore.model.Location;
import java.io.IOException;

/**
 * Assumes the currently selected slot is food and attempts to eat it
 *
 */
public class EatingAction extends AbstractAction {

	private int totalTicks;
	private int ticksLeftToWait;

	public EatingAction(ServerConnection connection, int ticksTaken) {
		super(connection);
		this.totalTicks = ticksTaken;
		this.ticksLeftToWait = ticksTaken;
	}

	@Override
	public void execute() {
		if (ticksLeftToWait == totalTicks) {
			try {
				BlockPlacementPacket eatingStarted = new BlockPlacementPacket(new Location(-1, 255, -1), -1);
				connection.sendPacket(eatingStarted);
			} catch (IOException e) {
				connection.getLogger().error("Failed to send eating start packet", e);
			}
		}
		if (ticksLeftToWait == 0) {
			try {
				PlayerDiggingPacket finishedEating = new PlayerDiggingPacket(5, new Location(0, 0, 0), (byte) 255);
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
