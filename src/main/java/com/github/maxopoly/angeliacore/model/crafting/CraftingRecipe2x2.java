package com.github.maxopoly.angeliacore.model.crafting;

import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.model.item.Material;

public final class CraftingRecipe2x2 extends CraftingRecipe {

	public CraftingRecipe2x2(ItemStack[] items, ItemStack result) {
		super(items, result);
		if (items.length != 4) {
			throw new IllegalArgumentException("2x2 crafting recipes require 4 slots");
		}
	}

	public CraftingRecipe2x2(Material[] mat, ItemStack result) {
		super(mat, result);
		if (mat.length != 4) {
			throw new IllegalArgumentException("2x2 crafting recipes require 4 slots");
		}
	}

}
