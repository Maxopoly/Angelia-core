package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.ItemStack;

public class PlayerInventory extends Inventory {

	public PlayerInventory() {
		super(46);
	}

	public ItemStack getHotbar(int slot) {
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Slot must be between 0 and 8");
		}
		return slots[36 + slot];
	}

	public void setOffHand(ItemStack is) {
		updateSlot(45, is);
	}

	public ItemStack getOffHand() {
		return slots[45];
	}

	/**
	 * Looks for the given item in any slot of the player hotbar. If the item is found, the index of the first find is
	 * returned, otherwise -1 is returned. Note that in other context the slot -1 might stand for the cursor, but that's
	 * not the case here. Note that the hotbar slot (0-8) is returned here and not the total slot!
	 * 
	 * @param is
	 *          ItemStack to look for
	 * @return Hotbar slot of the first find of the stack or -1 if none was found
	 */
	public short findHotbarSlot(ItemStack is) {
		for (short i = 0; i < 9; i++) {
			if (getHotbar(i).equals(is)) {
				return i;
			}
		}
		return -1;
	}

}
