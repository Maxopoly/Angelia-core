package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.EntityActionPacket;
import com.github.maxopoly.angeliacore.connection.play.packets.out.EntityActionPacket.Action;

public class StartSprinting extends AbstractAction {

	public StartSprinting(ServerConnection connection) {
		super(connection);
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new EntityActionPacket(connection.getPlayerStatus().getID(), Action.START_SPRINTING, 0));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send start sprint packet ", e);
		}
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] {ActionLock.MOVEMENT};
	}

}
