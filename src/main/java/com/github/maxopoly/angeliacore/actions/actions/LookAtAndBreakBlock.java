package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.SequentialActionExecution;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.BlockFace;
import com.github.maxopoly.angeliacore.model.Location;

public class LookAtAndBreakBlock extends AbstractAction {

	private SequentialActionExecution actions;
	private Location block;
	private int breakTime;

	public LookAtAndBreakBlock(ServerConnection connection, Location block, int breakTime) {
		super(connection);
		this.block = block.toBlockLocation();
		this.breakTime = breakTime;
		// we dont want to do the location based calculations when creating this instance (and queueing it), but instead
		// when it is executed
	}

	@Override
	public void execute() {
		if (actions == null) {
			setupActions();
		}
		actions.execute();
	}

	@Override
	public boolean isDone() {
		if (actions == null) {
			setupActions();
		}
		return actions.isDone();
	}

	private void setupActions() {
		Location playerLoc = connection.getPlayerStatus().getHeadLocation();
		BlockFace side = BlockFace.getRelative(playerLoc, block.getBlockCenter());
		this.actions = new SequentialActionExecution(new ChangeViewingDirection(connection, block.getBlockCenter()),
				new BreakBlock(connection, block, breakTime, side));
	}

}
