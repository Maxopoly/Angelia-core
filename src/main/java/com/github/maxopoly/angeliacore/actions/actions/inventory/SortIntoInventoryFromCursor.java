package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.inventory.DummyInventory;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;

/**
 * Sorts a given item into the given inventory by filling up empty and not completly filled stacks of the same type.
 * Always use Inventory.hasSpaceFor() before using this
 *
 */
public class SortIntoInventoryFromCursor extends InventoryAction {

	private byte windowID;
	private ItemStack item;
	private boolean done;
	private ClickInventory invClick;

	public SortIntoInventoryFromCursor(ServerConnection connection, byte windowID, ItemStack item) {
		super(connection);
		this.windowID = windowID;
		this.item = item;
		this.done = false;
	}

	@Override
	public void execute() {
		Inventory inv = connection.getPlayerStatus().getInventory(windowID);
		if (inv == null) {
			successfull = false;
			done = true;
		}
		if (invClick != null) {
			if (!invClick.isDone()) {
				invClick.execute();
				return;
			}
			invClick = null;
		}
		if (item.getAmount() <= 0) {
			successfull = true;
			done = true;
			return;
		}
		DummyInventory storage = inv.getPlayerStorage();
		for (int i = 0; i < storage.getSize(); i++) {
			ItemStack invSlot = storage.getSlot(i);
			if (invSlot == null || invSlot.isEmpty()) {
				invClick = new ClickInventory(connection, windowID, inv.translateStorageSlotToTotal(i), (byte) 0, 0, invSlot);
				inv.updateSlot(i, item.clone());
				item.setAmount(0);
				invClick.execute();
				break;
			}
			if (invSlot.equals(item)) {
				int amountToMove = item.getMaterial().getMaximumStackSize() - invSlot.getAmount();
				if (amountToMove > 0) {
					invClick = new ClickInventory(connection, windowID, inv.translateStorageSlotToTotal(i), (byte) 0, 0, invSlot);
					item.setAmount(item.getAmount() - amountToMove);
					invClick.execute();
					break;
				}
			}
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}

}
