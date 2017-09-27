package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class UpdateInventoryEvent implements AngeliaEvent {

	private Inventory inv;
	private byte invId;
	private ItemStack[] newContent;

	public UpdateInventoryEvent(Inventory inv, byte invId, ItemStack[] newContent) {
		this.invId = invId;
		this.inv = inv;
		this.newContent = newContent;
	}

	public byte getInventoryID() {
		return invId;
	}

	public Inventory getInventory() {
		return inv;
	}

	public ItemStack[] getContent() {
		return newContent;
	}
}
