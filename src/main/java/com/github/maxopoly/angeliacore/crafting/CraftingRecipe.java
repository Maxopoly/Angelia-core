package com.github.maxopoly.angeliacore.crafting;

import com.github.maxopoly.angeliacore.model.ItemStack;
import com.github.maxopoly.angeliacore.model.Material;
import com.github.maxopoly.angeliacore.model.inventory.DummyInventory;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;

public abstract class CraftingRecipe {

	public static final CraftingRecipe3x3 STONE_PICK = new CraftingRecipe3x3(new Material[] { Material.COBBLESTONE,
			Material.COBBLESTONE, Material.COBBLESTONE, Material.EMPTY_SLOT, Material.STICK, Material.EMPTY_SLOT,
			Material.EMPTY_SLOT, Material.STICK, Material.EMPTY_SLOT }, new ItemStack(Material.STONE_PICKAXE));
	public static final CraftingRecipe3x3 IRON_PICK = new CraftingRecipe3x3(new Material[] { Material.IRON_INGOT,
			Material.IRON_INGOT, Material.IRON_INGOT, Material.EMPTY_SLOT, Material.STICK, Material.EMPTY_SLOT,
			Material.EMPTY_SLOT, Material.STICK, Material.EMPTY_SLOT }, new ItemStack(Material.STONE_PICKAXE));
	public static final CraftingRecipe3x3 DIAMOND_PICK = new CraftingRecipe3x3(new Material[] { Material.DIAMOND,
			Material.DIAMOND, Material.DIAMOND, Material.EMPTY_SLOT, Material.STICK, Material.EMPTY_SLOT,
			Material.EMPTY_SLOT, Material.STICK, Material.EMPTY_SLOT }, new ItemStack(Material.STONE_PICKAXE));
	public static final CraftingRecipe2x2 CLAY_BLOCK = new CraftingRecipe2x2(new Material[] { Material.CLAY_BALL,
			Material.CLAY_BALL, Material.CLAY_BALL, Material.CLAY_BALL }, new ItemStack(Material.CLAY));

	private Inventory items;
	private Inventory combinedItems;
	private ItemStack result;

	public CraftingRecipe(ItemStack[] items, ItemStack result) {
		this.items = new DummyInventory(items.length);
		this.result = result;
		for (int i = 0; i < items.length; i++) {
			ItemStack is = items[i];
			if (is == null) {
				is = new ItemStack(Material.EMPTY_SLOT);
			}
			this.items.updateSlot(i, is);
		}
		this.combinedItems = this.items.compress();
	}

	public CraftingRecipe(Material[] mats, ItemStack result) {
		this(fromMaterials(mats), result);
	}

	private static ItemStack[] fromMaterials(Material[] mats) {
		ItemStack[] items = new ItemStack[mats.length];
		for (int i = 0; i < mats.length; i++) {
			items[i] = new ItemStack(mats[i]);
		}
		return items;
	}

	public ItemStack getIngredient(int slot) {
		return items.getSlot(slot);
	}

	public int amountAvailable(Inventory inv) {
		Inventory compressed = inv.compress();
		int runsPossible = Integer.MAX_VALUE;
		for (ItemStack craftingStack : combinedItems) {
			if (craftingStack.isEmpty()) {
				continue;
			}
			boolean found = false;
			for (ItemStack is : compressed) {
				if (is.equals(craftingStack)) {
					runsPossible = Math.min(runsPossible, is.getAmount() / craftingStack.getAmount());
					found = true;
					break;
				}
			}
			if (!found) {
				return 0;
			}
		}
		return runsPossible;
	}

	public int getSize() {
		return items.getSize();
	}

	public ItemStack getResult() {
		return result;
	}

}
