package com.github.maxopoly.angeliacore.model.inventory;

import java.util.Arrays;

public class HopperInventory extends Inventory {

	public HopperInventory(byte windowID) {
		super(41, windowID);
	}
	
	public DummyInventory getHopperStorage() {
		return new DummyInventory(Arrays.copyOfRange(slots, 0, 5));
	}

	public short translateHopperSlotToTotal(int slot) {
		if (slot >= 4) {
			throw new IllegalArgumentException("Tried to access slot with index " + slot + ", but hopper size is 4");
		}
		return (short) slot;
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 5;
	}

}
