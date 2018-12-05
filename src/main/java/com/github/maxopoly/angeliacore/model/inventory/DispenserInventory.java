package com.github.maxopoly.angeliacore.model.inventory;

import java.util.Arrays;

public class DispenserInventory extends Inventory {
	
	public DispenserInventory(byte windowID) {
		super(45, windowID);
	}
	
	public DummyInventory getDispenserStorage() {
		return new DummyInventory(Arrays.copyOfRange(slots, 0, 9));
	}

	public short translateDispenserSlotToTotal(int slot) {
		if (slot >= 9) {
			throw new IllegalArgumentException("Tried to access slot with index " + slot + ", but size is 9");
		}
		return (short) slot;
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 9;
	}
}
