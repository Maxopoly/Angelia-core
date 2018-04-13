package com.github.maxopoly.angeliacore.actions.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.location.Location;

public class WalkTo extends MoveTo {

	public WalkTo(ServerConnection connection, Location desto) {
		super(connection, desto, MoveTo.WALKING_SPEED);
	}
	
	
}