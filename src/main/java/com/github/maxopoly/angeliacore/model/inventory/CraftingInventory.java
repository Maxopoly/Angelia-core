package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

/**
 * An inventory in which items can be crafted, so either the normal player inventory or a crafting table inventory
 *
 */
public interface CraftingInventory {

	/**
	 * @return The crafting slots of the inventory without the result
	 */
	public DummyInventory getCraftingSlots();

	/**
	 * @return The result slot of this crafting inventory
	 */
	public ItemStack getCraftingResult();

	/**
	 * @return The index number of the slot in which the crafting result is
	 */
	public short getCraftingResultID();

	/**
	 * Translates a slot in the crafting section of the inventory to an absolute slot in the inventory
	 * 
	 * @param slot
	 *          Relative slot in the crafting grid
	 * @return Absolute slot
	 */
	public short translateCraftingSlotToTotal(int slot);

}
