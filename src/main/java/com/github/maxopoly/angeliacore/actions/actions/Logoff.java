package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.DisconnectReason;
import com.github.maxopoly.angeliacore.connection.ServerConnection;

public class Logoff extends AbstractAction {

	public Logoff(ServerConnection connection) {
		super(connection);
	}

	@Override
	public void execute() {
		connection.close(DisconnectReason.Intentional_Disconnect);
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public ActionLock[] getActionLocks() {
		// everything so it's always done at the end
		return new ActionLock[] { ActionLock.EVERYTHING };
	}

}
