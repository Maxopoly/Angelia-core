package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.location.Vector;

public class MoveRelative extends MoveTo {
	private Vector offset;

	public MoveRelative(ServerConnection connection, Vector offset, double speed) {
		super(connection, null, speed);
		this.offset = offset;
	}

	@Override
	public void execute() {
		if (this.destination == null) {
			//set on execute, not queue
			this.destination = connection.getPlayerStatus().getLocation().add(offset);
		}
		super.execute();
	}
}
