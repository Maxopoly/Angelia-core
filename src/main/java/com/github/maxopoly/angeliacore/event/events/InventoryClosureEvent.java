package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.model.inventory.Inventory;

public class InventoryClosureEvent implements AngeliaEvent {

	private byte id;
	private Inventory inv;

	public InventoryClosureEvent(byte id, Inventory inv) {
		this.id = id;
		this.inv = inv;
	}

	public byte getClosedInventoryId() {
		return id;
	}

	public Inventory getClosedInventory() {
		return inv;
	}

}
