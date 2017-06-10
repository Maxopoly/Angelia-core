package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.actions.AbstractAction;

import com.github.maxopoly.angeliacore.connection.ServerConnection;

public class Wait extends AbstractAction {

	private int ticksLeft;

	public Wait(ServerConnection connection, int ticksToWait) {
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
