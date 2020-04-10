package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

/**
 * Represents a one time run action, can be used for things like lambdas
 */
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

	/**
	 * Executes the action
	 */
	public abstract void executeAction();

	@Override
	public boolean isDone() {
		return hasRun;
	}

}
