package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class AnvilInventory extends Inventory {

	public AnvilInventory(byte windowID) {
		super(39, windowID);
	}
	
	public ItemStack getFirstCombiningSlot() {
		return slots [0];
	}
	
	public ItemStack getSecondCombiningSlot() {
		return slots [1];
	}
	
	public ItemStack getResultSlot() {
		return slots [2];
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 3;
	}

}