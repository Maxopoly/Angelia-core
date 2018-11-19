package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.Enchantment;
import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class EnchantmentTableInventory extends Inventory {
	
	Enchantment enchant;

	public EnchantmentTableInventory(byte windowID) {
		super(38, windowID);
	}
	
	public ItemStack getEnchantingSlot() {
		return slots [0];
	}
	
	public ItemStack getLapisSlot() {
		return slots [1];
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 2;
	}

}
