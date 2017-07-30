package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

import java.util.Arrays;

public class CraftingTableInventory extends Inventory implements CraftingInventory {

	public CraftingTableInventory() {
		super(45);
	}

	@Override
	public DummyInventory getCraftingSlots() {
		return new DummyInventory(Arrays.copyOfRange(slots, 1, 9));
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
	public short translateCraftingSlotToTotal(int slot) {
		return (short) (slot + 1);
	}

	@Override
	public DummyInventory getHotbar() {
		return new DummyInventory(Arrays.copyOfRange(slots, 37, 45));
	}

	@Override
	public DummyInventory getPlayerStorage() {
		return new DummyInventory(Arrays.copyOfRange(slots, 10, 45));
	}

	@Override
	public DummyInventory getPlayerStorageWithoutHotbar() {
		return new DummyInventory(Arrays.copyOfRange(slots, 10, 36));
	}

	@Override
	public short translateStorageSlotToTotal(int slot) {
		return (short) (slot + 10);
	}

	@Override
	public short translateHotbarToTotal(int slot) {
		return (short) (slot + 37);
	}

}
