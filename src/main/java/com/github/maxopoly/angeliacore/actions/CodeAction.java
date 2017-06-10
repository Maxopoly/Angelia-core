package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public abstract class CodeAction extends AbstractAction {

	public CodeAction(ServerConnection connection) {
		super(connection);
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
