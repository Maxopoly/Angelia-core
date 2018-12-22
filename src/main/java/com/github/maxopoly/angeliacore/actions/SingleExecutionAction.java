package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public abstract class SingleExecutionAction extends AbstractAction {

	private boolean hasRun;

	public SingleExecutionAction(ServerConnection connection) {
		super(connection);
		hasRun = false;
	}

	@Override
	public void execute() {
		executeAction();
		hasRun = true;
	}

	public abstract void executeAction();

	@Override
	public boolean isDone() {
		return hasRun;
	}

}
