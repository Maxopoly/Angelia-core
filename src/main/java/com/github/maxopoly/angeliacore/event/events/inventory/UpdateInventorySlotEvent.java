package com.github.maxopoly.angeliacore.event.events.inventory;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class UpdateInventorySlotEvent implements AngeliaEvent {

	private Inventory inv;
	private byte invId;
	private int slot;
	private ItemStack newContent;

	public UpdateInventorySlotEvent(Inventory inv, byte invId, int slot, ItemStack newContent) {
		this.invId = invId;
		this.inv = inv;
		this.slot = slot;
		this.newContent = newContent;
	}

	public int getSlot() {
		return slot;
	}

	public byte getInventoryID() {
		return invId;
	}

	public Inventory getInventory() {
		return inv;
	}

	public ItemStack getNewSlotContent() {
		return newContent;
	}

	public ItemStack getOldSlotContent() {
		return inv.getSlot(slot);
	}

}
