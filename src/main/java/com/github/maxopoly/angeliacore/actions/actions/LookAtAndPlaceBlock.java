package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.actions.SequentialActionExecution;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.model.location.Location;

public class LookAtAndPlaceBlock extends AbstractAction {

	private SequentialActionExecution actions;
	private Location block;
	private BlockFace face;

	public LookAtAndPlaceBlock(ServerConnection connection, Location block) {
		this(connection, block, null);
	}

	public LookAtAndPlaceBlock(ServerConnection connection, Location block, BlockFace face) {
		super(connection);
		this.block = block.toBlockLocation();
		this.face = face;
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
		BlockFace side;
		if (face == null) {
			side = BlockFace.getRelative(playerLoc, block.getBlockCenter());
		} else {
			side = face;
		}
		this.actions = new SequentialActionExecution(new LookAt(connection, block, side), new PlaceBlock(connection, block,
				side));
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.LOOKING_DIRECTION, ActionLock.HOTBAR_SLOT };
	}

}
