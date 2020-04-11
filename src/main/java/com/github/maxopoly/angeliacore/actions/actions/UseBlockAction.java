package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.BreakAnimationPacket;
import com.github.maxopoly.angeliacore.model.location.Location;

public class UseBlockAction extends AbstractAction {

	private Location location;

	public UseBlockAction(ServerConnection connection, Location loc) {
		super(connection);
		this.location = loc;
	}

	@Override
	public void execute() {
		try {
			// Shouldn't this also have 2 separate actions?
			new LookAt(connection, location).execute();
			connection.sendPacket(new BreakAnimationPacket());
		} catch (IOException e) {
			connection.getLogger().error("Failed to send block usage", e);
		}
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.HOTBAR_SLOT };
	}

	@Override
	public boolean isDone() {
		return true; // Always 1 tick
	}

}
