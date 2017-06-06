package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.ItemStack;

public class Inventory {

	protected ItemStack[] slots;

	public Inventory(int size) {
		slots = new ItemStack[size];
	}

	public void setSlots(ItemStack[] content) {
		if (content.length != slots.length) {
			throw new IllegalArgumentException("Slot length can't be different");
		}
	}

	public void updateSlot(int id, ItemStack is) {
		slots[id] = is;
	}

}
