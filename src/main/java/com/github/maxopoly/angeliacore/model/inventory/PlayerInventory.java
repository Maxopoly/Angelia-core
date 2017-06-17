package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.ItemStack;
import java.util.Arrays;

public class PlayerInventory extends Inventory implements CraftingInventory {

	public PlayerInventory() {
		super(46);
	}

	public void setOffHand(ItemStack is) {
		updateSlot(45, is);
	}

	public ItemStack getOffHand() {
		return slots[45];
	}

	@Override
	public short translateStorageSlotToTotal(int slot) {
		if (slot < 0 || slot > 35) {
			throw new IllegalArgumentException("Invalid slot " + slot + " is not a storage slot");
		}
		return (short) (slot + 9);
	}

	@Override
	public short translateHotbarToTotal(int slot) {
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Invalid slot " + slot + " is not a hotbar slot");
		}
		return (short) (slot + 36);
	}

	@Override
	public DummyInventory getHotbar() {
		return new DummyInventory(Arrays.copyOfRange(slots, 36, 44));
	}

	@Override
	public DummyInventory getPlayerStorage() {
		return new DummyInventory(Arrays.copyOfRange(slots, 9, 44));
	}

	@Override
	public DummyInventory getPlayerStorageWithoutHotbar() {
		return new DummyInventory(Arrays.copyOfRange(slots, 9, 35));
	}

	@Override
	public DummyInventory getCraftingSlots() {
		return new DummyInventory(Arrays.copyOfRange(slots, 1, 4));
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

}
