package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.model.location.Location;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.SequentialActionExecution;
import com.github.maxopoly.angeliacore.connection.ServerConnection;

/**
 * Digs straight down. The player is assumed to already be at the location given
 *
 */
public class DigDown extends SequentialActionExecution {

	/**
	 * @param loc
	 *          Current player location and block location of the block above the first block to dif
	 * @param howFar
	 *          How deep should be dug
	 */
	public DigDown(ServerConnection connection, Location loc, int howFar, int blockBreakTime) {
		// we have to do this inline while referring to a static method, because Java actually wont allow it any other way
		super(constructActions(connection, loc, howFar, blockBreakTime));
	}

	private static AbstractAction[] constructActions(ServerConnection connection, Location loc, int howFar,
			int blockBreakTime) {
		if (howFar <= 0) {
			throw new IllegalArgumentException("Must dig at least one block deep");
		}
		AbstractAction[] actions = new AbstractAction[howFar * 2];
		for (int i = 0; i < howFar; i++) {
			Location block = new Location(loc.getBlockX(), loc.getBlockY() - (i + 1), loc.getBlockZ());
			actions[(i * 2)] = new LookAtAndBreakBlock(connection, block, blockBreakTime);
			actions[(i * 2) + 1] = new MoveTo(connection, block.getBlockCenterXZ(), MoveTo.FALLING);
		}
		return actions;
	}
}
