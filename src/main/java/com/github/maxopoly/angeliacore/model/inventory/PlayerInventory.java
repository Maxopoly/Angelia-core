package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

import java.util.Arrays;

public class PlayerInventory extends Inventory implements CraftingInventory {

	public PlayerInventory(byte windowID) {
		super(46, windowID);
	}

	public void setOffHand(ItemStack is) {
		updateSlot(45, is);
	}

	public ItemStack getOffHand() {
		return slots[45];
	}

	@Override
	public DummyInventory getCraftingSlots() {
		return new DummyInventory(Arrays.copyOfRange(slots, 1, 5));
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
	protected int getPlayerStorageStartingSlot() {
		return 9;
	}

}
