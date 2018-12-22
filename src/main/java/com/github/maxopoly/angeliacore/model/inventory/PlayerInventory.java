package com.github.maxopoly.angeliacore.model.inventory;

import java.util.Arrays;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class PlayerInventory extends Inventory implements CraftingInventory {

	public PlayerInventory() {
		super(46, (byte) 0);
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
		return new DummyInventory(Arrays.copyOfRange(slots, 1, 5));
	}

	public ItemStack getOffHand() {
		return slots[45];
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 9;
	}

	public void setOffHand(ItemStack is) {
		updateSlot(45, is);
	}

	@Override
	public short translateCraftingSlotToTotal(int slot) {
		return (short) (slot + 1);
	}

}
