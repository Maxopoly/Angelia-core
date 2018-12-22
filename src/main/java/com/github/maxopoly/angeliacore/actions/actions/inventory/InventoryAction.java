package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;

public abstract class InventoryAction extends AbstractAction {

	protected boolean successfull;

	public InventoryAction(ServerConnection connection) {
		super(connection);
		this.successfull = false;
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.INVENTORY, ActionLock.HOTBAR_SLOT };
	}

	/**
	 * @return Whether the item transaction completly worked
	 */
	public boolean wasSuccessfull() {
		return successfull;
	}

}
