package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;

public abstract class InventoryAction extends AbstractAction {

	protected boolean successful;

	public InventoryAction(ServerConnection connection) {
		super(connection);
		this.successful = false;
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.INVENTORY, ActionLock.HOTBAR_SLOT };
	}

	/**
	 * @return Whether the item transaction completely worked
	 */
	public boolean wasSuccessful() {
		return successful;
	}

}
