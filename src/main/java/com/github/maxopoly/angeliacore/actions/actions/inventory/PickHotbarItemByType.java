package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.actions.ActionLock;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.model.item.Material;

/**
 * Selects an item on the hotbar with the given id
 *
 */
public class PickHotbarItemByType extends AbstractAction {

	private Material mat;
	private boolean found;

	public PickHotbarItemByType(ServerConnection connection, Material mat) {
		super(connection);
		this.mat = mat;
		this.found = false;
	}

	@Override
	public void execute() {
		PlayerInventory inv = connection.getPlayerStatus().getPlayerInventory();
		int hotbarSlot = inv.getHotbar().findSlotByType(new ItemStack(mat));
		if (hotbarSlot == -1) {
			found = false;
			return;
		}
		// we found a slot, so lets switch to it if it isnt already selected
		found = true;
		if (connection.getPlayerStatus().getSelectedHotbarSlot() == hotbarSlot) {
			return;
		}
		new ChangeSelectedItem(connection, hotbarSlot).execute();
	}

	@Override
	public ActionLock[] getActionLocks() {
		return new ActionLock[] { ActionLock.HOTBAR_SLOT };
	}

	@Override
	public boolean isDone() {
		return true;
	}

	/**
	 * @return Whether a slot with the requested item is selected after this action
	 */
	public boolean wasFound() {
		return found;
	}

}
