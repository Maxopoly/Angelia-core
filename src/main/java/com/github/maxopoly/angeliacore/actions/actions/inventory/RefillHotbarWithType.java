package com.github.maxopoly.angeliacore.actions.actions.inventory;

import com.github.maxopoly.angeliacore.model.item.Material;

import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;

/**
 * Opens the inventory and moves a single stack of items of a certain type from the storage section of the inventory on
 * the hotbar
 *
 */
public class RefillHotbarWithType extends InventoryAction {

	private Material mat;
	private MoveItem move;
	private int originSlot;
	private int targetSlot;
	private ItemStack movedStack;
	private boolean done;

	public RefillHotbarWithType(ServerConnection connection, Material mat) {
		super(connection);
		this.mat = mat;
		this.done = false;
	}

	@Override
	public void execute() {
		if (move == null) {
			PlayerInventory playerInv = connection.getPlayerStatus().getPlayerInventory();
			Inventory storage = playerInv.getPlayerStorageWithoutHotbar();
			int index = storage.findSlotByType(new ItemStack(mat));
			if (index == -1) {
				successfull = false;
				done = true;
				return;
			}
			movedStack = storage.getSlot(index);
			originSlot = playerInv.translateStorageSlotToTotal(index);
			// find empty slot
			short targetHotbarSlot = playerInv.getHotbar().findSlotByType(new ItemStack(Material.EMPTY_SLOT));
			if (targetHotbarSlot == -1) {
				successfull = false;
				done = true;
				return;
			}
			targetSlot = playerInv.translateHotbarToTotal(targetHotbarSlot);
			move = new MoveItem(connection, (byte) 0, originSlot, targetSlot);
			new OpenPlayerInventory(connection).execute();
			move.execute();
		} else {
			move.execute();
			if (move.isDone()) {
				if (move.wasSuccessfull()) {
					successfull = true;
					// update clientside model
					PlayerInventory inv = connection.getPlayerStatus().getPlayerInventory();
					inv.updateSlot(originSlot, new ItemStack(Material.EMPTY_SLOT));
					inv.updateSlot(originSlot, movedStack);
				} else {
					successfull = false;
				}
				done = true;
			}
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}
}
