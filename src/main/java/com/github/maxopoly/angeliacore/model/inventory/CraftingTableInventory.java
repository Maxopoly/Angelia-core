package com.github.maxopoly.angeliacore.model.inventory;

import java.util.Arrays;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class CraftingTableInventory extends Inventory implements CraftingInventory {

	public CraftingTableInventory(byte windowID) {
		super(45, windowID);
	}

	@Override
	public ItemStack getCraftingResult() {
		return slots[0];
	}

	@Override
	public short getCraftingResultID() {
		return 0;
	}

	@Override
	public DummyInventory getCraftingSlots() {
		return new DummyInventory(Arrays.copyOfRange(slots, 1, 10));
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 10;
	}

	@Override
	public short translateCraftingSlotToTotal(int slot) {
		return (short) (slot + 1);
	}
}
