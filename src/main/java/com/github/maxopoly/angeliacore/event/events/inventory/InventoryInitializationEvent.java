package com.github.maxopoly.angeliacore.event.events.inventory;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;

public class InventoryInitializationEvent implements AngeliaEvent {

	private Inventory inv;
	private byte invId;

	public InventoryInitializationEvent(Inventory inv, byte invId) {
		this.invId = invId;
		this.inv = inv;
	}

	public byte getInventoryID() {
		return invId;
	}

	public Inventory getInventory() {
		return inv;
	}
}
