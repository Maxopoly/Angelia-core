package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.connection.ServerConnection;

public class Logoff extends AbstractAction {

	public Logoff(ServerConnection connection) {
		super(connection);
	}

	@Override
	public void execute() {
		connection.close();
		System.exit(0);
	}

	@Override
	public boolean isDone() {
		return false;
	}

}
