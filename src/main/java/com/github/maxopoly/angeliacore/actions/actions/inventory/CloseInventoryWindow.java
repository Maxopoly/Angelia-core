package com.github.maxopoly.angeliacore.actions.actions.inventory;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.actions.SingleExecutionAction;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.CloseWindowPacket;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;

public class CloseInventoryWindow extends SingleExecutionAction {

	private byte windowID;

	public CloseInventoryWindow(ServerConnection connection, Inventory inventory) {
		super(connection);
		this.windowID = inventory.getWindowID();
	}

	@Override
	public void executeAction() {
		try {
			connection.sendPacket(new CloseWindowPacket(windowID));
		} catch (IOException e) {
			connection.getLogger().error("Failed to send inventory close packet", e);
		}
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.INVENTORY, ActionLock.HOTBAR_SLOT };
	}

}
