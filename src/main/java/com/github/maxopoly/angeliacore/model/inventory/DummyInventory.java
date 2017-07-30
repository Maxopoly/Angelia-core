package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

import org.apache.commons.lang3.NotImplementedException;

/**
 * Not an actual inventory, but just convenient storage for ItemStacks *
 */
public class DummyInventory extends Inventory {

	public DummyInventory(int size) {
		super(size);
	}

	public DummyInventory(ItemStack[] items) {
		super(items);
	}

	@Override
	public DummyInventory getPlayerStorage() {
		throw new NotImplementedException("Just a dummy");
	}

	@Override
	public DummyInventory getPlayerStorageWithoutHotbar() {
		throw new NotImplementedException("Just a dummy");
	}

	@Override
	public short translateStorageSlotToTotal(int slot) {
		throw new NotImplementedException("Just a dummy");
	}

	@Override
	public short translateHotbarToTotal(int slot) {
		throw new NotImplementedException("Just a dummy");
	}

	@Override
	public DummyInventory getHotbar() {
		throw new NotImplementedException("Just a dummy");
	}

}
