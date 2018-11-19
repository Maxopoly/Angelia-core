package com.github.maxopoly.angeliacore.event.events.inventory;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;

public class OpenInventoryEvent implements AngeliaEvent {

	private Inventory inv;
	private byte invID;

	public OpenInventoryEvent(Inventory inv, byte invID) {
		this.inv = inv;
		this.invID = invID;
	}

	public byte getInventoryID() {
		return invID;
	}

	public Inventory getInventory() {
		return inv;
	}

}
