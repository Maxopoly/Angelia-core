package com.github.maxopoly.angeliacore.model.inventory;

import java.util.Arrays;

public class ChestInventory extends Inventory {

	private int rows;
	private String name;

	public ChestInventory(int rows, String name, byte windowID) {
		super((rows + 4) * 9, windowID);
		this.rows = rows;
		this.name = name;

	}

	public DummyInventory getChestStorage() {
		return new DummyInventory(Arrays.copyOfRange(slots, 0, (rows * 9)));
	}

	public short translateChestStorageSlotToTotal(int slot) {
		if (slot >= rows * 9) {
			throw new IllegalArgumentException("Tried to access slot with index " + slot + ", but inventory size is " + rows * 9);
		}
		return (short) slot;
	}

	public int getRowCount() {
		return rows;
	}

	public String getName() {
		return name;
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return rows * 9;
	}

}
