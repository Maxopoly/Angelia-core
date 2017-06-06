package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public abstract class AbstractAction {

	protected ServerConnection connection;

	public AbstractAction(ServerConnection connection) {
		this.connection = connection;
	}

	public abstract void execute();

	public abstract boolean isDone();
}
