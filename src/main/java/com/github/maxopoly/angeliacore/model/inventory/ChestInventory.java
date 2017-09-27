package com.github.maxopoly.angeliacore.model.inventory;

import java.util.Arrays;

public class ChestInventory extends Inventory {

	private int rows;
	private String name;

	public ChestInventory(int rows, String name) {
		// add inventory as well
		super((rows + 4) * 9);
		this.rows = rows;
		this.name = name;

	}

	@Override
	public DummyInventory getHotbar() {
		return new DummyInventory(Arrays.copyOfRange(slots, (rows + 3) * 9, ((rows + 4) * 9) - 1));
	}

	@Override
	public DummyInventory getPlayerStorage() {
		return new DummyInventory(Arrays.copyOfRange(slots, rows * 9, ((rows + 4) * 9) - 1));
	}

	@Override
	public DummyInventory getPlayerStorageWithoutHotbar() {
		return new DummyInventory(Arrays.copyOfRange(slots, rows * 9, ((rows + 3) * 9) - 1));
	}

	@Override
	public short translateStorageSlotToTotal(int slot) {
		return (short) (slot + (rows * 9));
	}

	@Override
	public short translateHotbarToTotal(int slot) {
		return (short) (slot + ((rows + 3) * 9));
	}

	public DummyInventory getChestStorage() {
		return new DummyInventory(Arrays.copyOfRange(slots, 0, (rows * 9) - 1));
	}

	public short translateChestStorageSlotToTotal(int slot) {
		return (short) slot;
	}

	public int getRowCount() {
		return rows;
	}

	public String getName() {
		return name;
	}

}
