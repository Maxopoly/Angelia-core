package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.SequentialActionExecution;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.BlockFace;
import com.github.maxopoly.angeliacore.model.Location;

public class LookAtAndPlaceBlock extends AbstractAction {

	private SequentialActionExecution actions;
	private Location block;

	public LookAtAndPlaceBlock(ServerConnection connection, Location block) {
		super(connection);
		this.block = block.toBlockLocation();
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
				new PlaceBlock(connection, block, side));
	}

}
