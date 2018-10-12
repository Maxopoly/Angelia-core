package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class FurnaceInventory extends Inventory {

	public FurnaceInventory(byte windowID) {
		super(39, windowID);
	}
	
	public ItemStack getFuelSlot() {
		return slots [1];
	}
	
	public ItemStack getIngredientSlot() {
		return slots [0];
	}
	
	public ItemStack getResultSlot() {
		return slots [2];
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 3;
	}

}
