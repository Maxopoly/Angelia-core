package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public class WaitingAction extends AbstractAction {

	private int ticksLeft;

	public WaitingAction(ServerConnection connection, int ticksToWait) {
		super(connection);
		ticksLeft = ticksToWait;
	}

	@Override
	public void execute() {
		ticksLeft--;
	}

	@Override
	public boolean isDone() {
		return ticksLeft <= 0;
	}

}
