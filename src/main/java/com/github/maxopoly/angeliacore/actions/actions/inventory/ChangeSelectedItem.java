package com.github.maxopoly.angeliacore.actions.actions.inventory;

import java.io.IOException;

import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.actions.SingleExecutionAction;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.connection.play.packets.out.HeldItemChangePacket;

public class ChangeSelectedItem extends SingleExecutionAction {

	private int slot;

	public ChangeSelectedItem(ServerConnection connection, int slot) {
		super(connection);
		this.slot = slot;
	}

	@Override
	public void executeAction() {
		try {
			connection.sendPacket(new HeldItemChangePacket(slot));
			connection.getPlayerStatus().setSelectedHotbarSlot(slot);
		} catch (IOException e) {
			connection.getLogger().error("Failed to update item held", e);
		}
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.HOTBAR_SLOT };
	}

}
