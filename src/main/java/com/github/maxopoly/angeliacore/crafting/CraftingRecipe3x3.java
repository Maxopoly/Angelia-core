package com.github.maxopoly.angeliacore.crafting;

import com.github.maxopoly.angeliacore.model.ItemStack;
import com.github.maxopoly.angeliacore.model.Material;

public final class CraftingRecipe3x3 extends CraftingRecipe {

	public CraftingRecipe3x3(ItemStack[] items, ItemStack result) {
		super(items, result);
		if (items.length != 9) {
			throw new IllegalArgumentException("3x3 crafting recipes require 9 slots");
		}
	}

	public CraftingRecipe3x3(Material[] mat, ItemStack result) {
		super(mat, result);
		if (mat.length != 9) {
			throw new IllegalArgumentException("3x3 crafting recipes require 9 slots");
		}
	}

}
