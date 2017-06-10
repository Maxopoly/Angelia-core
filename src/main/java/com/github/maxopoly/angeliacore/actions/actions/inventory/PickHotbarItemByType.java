package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.actions.AbstractAction;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.ItemStack;
import com.github.maxopoly.angeliacore.model.Material;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;

/**
 * Selects an item on the hotbar with the given id
 *
 */
public class PickHotbarItemByType extends AbstractAction {

	private short id;
	private boolean found;

	public PickHotbarItemByType(ServerConnection connection, short id) {
		super(connection);
		this.id = id;
		this.found = false;
	}

	@Override
	public void execute() {
		PlayerInventory inv = connection.getPlayerStatus().getPlayerInventory();
		int hotbarSlot = inv.getHotbar().findSlotByType(new ItemStack(Material.getByID(id)));
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
