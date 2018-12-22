package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.SequentialActionExecution;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.location.Location;

/**
 * Digs straight down. The player is assumed to already be at the location given
 *
 */
public class DigDown extends SequentialActionExecution {

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

	/**
	 * Digs straight down, assuming all blocks in the way have the same hardness
	 * 
	 * @param connection     Current connection
	 * @param loc            Player location to start from
	 * @param howFar         How many blocks should be dug down
	 * @param blockBreakTime Break time in ticks spent for each block
	 */
	public DigDown(ServerConnection connection, Location loc, int howFar, int blockBreakTime) {
		// we have to do this inline while referring to a static method, because Java
		// actually wont allow it any other way
		super(constructActions(connection, loc, howFar, blockBreakTime));
	}
}
