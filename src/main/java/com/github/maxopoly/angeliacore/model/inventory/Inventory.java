package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.ItemStack;

public class Inventory {

	protected ItemStack[] slots;
	private static ItemStack cursor;

	public Inventory(int size) {
		slots = new ItemStack[size];
	}

	public void setSlots(ItemStack[] content) {
		if (content.length != slots.length) {
			throw new IllegalArgumentException("Slot length can't be different");
		}
	}

	public void updateSlot(int id, ItemStack is) {
		if (id == -1) {
			updateCursor(is);
			return;
		}
		slots[id] = is;
	}

	/**
	 * Looks for the given item in any slot of this inventory. If the item is found, the index of the first find is
	 * returned, otherwise -1 is returned. Note that in other context the slot -1 might stand for the cursor, but that's
	 * not the case here
	 * 
	 * @param is
	 *          ItemStack to look for
	 * @return Slot of the first find of the stack or -1 if none was found
	 */
	public short findSlot(ItemStack is) {
		for (short i = 0; i < slots.length; i++) {
			if (slots[i].equals(is)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the content of the slot with the given id. If the slot is empty, a dummy ItemStack with an id of -1 is
	 * returned
	 * 
	 * @param id
	 *          Slot id
	 * @return ItemStack representing the content of the selected slot
	 */
	public ItemStack getSlot(int id) {
		ItemStack is = slots[id];
		return is == null ? new ItemStack((short) -1) : is;
	}

	public void updateCursor(ItemStack is) {
		this.cursor = is;
	}

	public ItemStack getCursor() {
		return cursor;
	}

}
