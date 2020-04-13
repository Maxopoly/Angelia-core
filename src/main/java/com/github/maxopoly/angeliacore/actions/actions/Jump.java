package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.location.Vector;

public class Jump extends AbstractAction {
	
	private double upAcceleration;

	public Jump(ServerConnection connection, double upAcceleration) {
		super(connection);
		this.upAcceleration = upAcceleration;
	}

	@Override
	public void execute() {
		connection.getPhysicsManager().addAcceleration(new Vector(0,upAcceleration ,0));
		
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.MOVEMENT };
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
