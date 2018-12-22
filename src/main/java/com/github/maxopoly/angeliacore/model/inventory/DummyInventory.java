package com.github.maxopoly.angeliacore.model.inventory;

import org.apache.commons.lang3.NotImplementedException;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

/**
 * Not an actual inventory, but just convenient storage for ItemStacks *
 */
public class DummyInventory extends Inventory {

	public DummyInventory(int size) {
		super(size, (byte) -1);
	}

	public DummyInventory(ItemStack[] items) {
		super(items, (byte) -1);
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		throw new NotImplementedException("Just a dummy");
	}

}
