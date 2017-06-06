package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.ItemStack;

public class PlayerInventory extends Inventory {

	public PlayerInventory() {
		super(46);
	}

	public ItemStack getHotbar(int slot) {
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Slot must be between 0 and 8");
		}
		return slots[36 + slot];
	}

	public void setOffHand(ItemStack is) {
		updateSlot(45, is);
	}

	public ItemStack getOffHand() {
		return slots[45];
	}

}
