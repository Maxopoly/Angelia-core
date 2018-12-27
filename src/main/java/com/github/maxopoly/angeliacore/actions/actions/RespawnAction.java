package com.github.maxopoly.angeliacore.actions.actions;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.ClientStatusPacket;

/**
 * Attempts to respawn
 *
 */
public class RespawnAction extends AbstractAction {

	public RespawnAction(ServerConnection connection) {
		super(connection);
	}
	
	@Override
	public void execute() {
		try {
			connection.sendPacket(new ClientStatusPacket(0));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send respawn packet", e);
		}

	}

	@Override
	public ActionLock[] getActionLocks() {
		return ActionLock.values();
	}

	@Override
	public boolean isDone() {
		return true;
	}
	

}
