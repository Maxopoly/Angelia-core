package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.actions.SequentialActionExecution;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.location.Location;

/**
 * 
 * Sprints to a certain location
 *
 */
public class SprintTo extends SequentialActionExecution {

	public SprintTo(ServerConnection connection, Location desto) {
		super(new StartSprinting(connection), new MoveTo(connection, desto, MoveTo.SPRINTING_SPEED),
				new StopSprinting(connection));
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.MOVEMENT };
	}
}
