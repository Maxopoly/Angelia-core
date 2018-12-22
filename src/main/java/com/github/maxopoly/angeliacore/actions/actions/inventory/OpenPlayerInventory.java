package com.github.maxopoly.angeliacore.actions.actions.inventory;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.ClientStatusPacket;

public class OpenPlayerInventory extends AbstractAction {

	public OpenPlayerInventory(ServerConnection connection) {
		super(connection);
	}

	@Override
	public void execute() {
		try {
			connection.sendPacket(new ClientStatusPacket(2));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send inventory opening packet");
		}
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.INVENTORY, ActionLock.HOTBAR_SLOT };
	}

	@Override
	public boolean isDone() {
		return true;
	}

}
