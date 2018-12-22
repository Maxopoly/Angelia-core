package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public abstract class AbstractAction {

	protected ServerConnection connection;

	public AbstractAction(ServerConnection connection) {
		this.connection = connection;
	}

	public abstract void execute();

	/**
	 * Each action may define certain properties, which it needs to operate.
	 * Multiple actions with completly separate action locks may be executed
	 * simultaneously
	 * 
	 * @return Action locks of this action
	 */
	public abstract ActionLock[] getActionLocks();

	public abstract boolean isDone();

}
