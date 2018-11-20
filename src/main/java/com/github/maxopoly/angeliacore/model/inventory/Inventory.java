package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.model.item.Material;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public abstract class Inventory implements Iterable<ItemStack> {

	private boolean isInitialized = false;

	public static Inventory constructInventory(String type, String name, byte size, byte windowID) {
		Inventory inv;
		switch (type) {
		case "minecraft:container":
			if (size % 9 != 0) {
				inv = null;
				break;
			}
			inv = new ChestInventory(size / 9, name, windowID);
			break;
		case "minecraft:chest":
			if (size % 9 != 0) {
				inv = null;
				break;
			}
			inv = new ChestInventory(size / 9, name, windowID);
			break;
		case "minecraft:crafting_table":
			inv = new CraftingTableInventory(windowID);
			break;
		case "minecraft:furnace":
			inv = new FurnaceInventory(windowID);
			break;
		case "minecraft:dispenser":
		case "minecraft:dropper":
			inv = new DispenserInventory(windowID);
			break;
		case "minecraft:enchanting_table":
			inv = new EnchantmentTableInventory(windowID);
			break;
		case "minecraft:hopper":
			inv = new HopperInventory(windowID);
			break;
		case "minecraft:beacon":
			inv = new BeaconInventory(windowID);
			break;
		case "minecraft:brewing_stand":
			inv = new BrewingStandInventory(windowID);
			break;
		default:
			inv = null;
			break;
		}
		return inv;
	}

	protected ItemStack[] slots;
	private byte windowID;
	private static ItemStack cursor;

	public Inventory(int size, byte windowID) {
		slots = new ItemStack[size];
		this.windowID = windowID;
	}

	public Inventory(ItemStack[] items, byte windowID) {
		this(items.length, windowID);
		isInitialized = true;
		for (int i = 0; i < items.length; i++) {
			updateSlot(i, items[i]);
		}
	}
	
	public byte getWindowID() {
		return windowID;
	}

	public void setSlots(ItemStack[] content) {
		if (content.length != slots.length) {
			throw new IllegalArgumentException("Slot length can't be different");
		}
		isInitialized = true;
		for (int i = 0; i < content.length; i++) {
			updateSlot(i, content[i]);
		}
	}

	public void updateSlot(int id, ItemStack is) {
		if (id == -1) {
			updateCursor(is);
			return;
		}
		if (is == null) {
			is = new ItemStack(Material.EMPTY_SLOT);
		}
		slots[id] = is;
	}

	/**
	 * @return How many slots this inventory has total
	 */
	public int getSize() {
		return slots.length;
	}

	/**
	 * Looks for the given item in any slot of this inventory. If the item is found,
	 * the index of the first find is returned, otherwise -1 is returned. Note that
	 * in other context the slot -1 might stand for the cursor, but that's not the
	 * case here
	 *
	 * @param is ItemStack to look for
	 * @return Slot of the first find of the stack or -1 if none was found
	 */
	public short findSlot(ItemStack is) {
		for (short i = 0; i < slots.length; i++) {
			if (getSlot(i).equals(is)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Looks for the given item id in any slot of this inventory. If the item is
	 * found, the index of the first find is returned, otherwise -1 is returned.
	 * Note that in other context the slot -1 might stand for the cursor, but that's
	 * not the case here
	 *
	 * @param is id of the ItemStack to look for
	 * @return Slot of the first find of the item id or -1 if none was found
	 */
	public short findSlotByType(ItemStack is) {
		for (short i = 0; i < slots.length; i++) {
			if (getSlot(i).getMaterial() == is.getMaterial()) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * After opening an inventory, it's content is sent in a separate packet. If
	 * this packet was received yet and the content of this chest is known, this
	 * will be true
	 * 
	 * @return Whether the chest content is initialized
	 */
	public boolean isInitialized() {
		return isInitialized;
	}

	/**
	 * Gets the content of the slot with the given id. If the slot is empty, a dummy
	 * ItemStack with an id of -1 is returned
	 *
	 * @param id Slot id
	 * @return ItemStack representing the content of the selected slot
	 */
	public ItemStack getSlot(int id) {
		ItemStack is = slots[id];
		return is == null ? new ItemStack(Material.EMPTY_SLOT) : is;
	}

	/**
	 * Checks whether the given ItemStack fits into this inventory by filling up
	 * existing stacks and using empty slot as available
	 *
	 * @param is ItemStack to try
	 * @return True if the ItemStack would fit in the inventory, false if not
	 */
	public boolean hasSpaceFor(ItemStack is) {
		int toSort = is.getAmount();
		for (ItemStack invSlot : this) {
			if (invSlot == null || invSlot.isEmpty()) {
				return true;
			}
			if (invSlot.equals(is)) {
				toSort -= (is.getMaterial().getMaximumStackSize() - invSlot.getAmount());
				if (toSort <= 0) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Compresses this instance by combining all ItemStacks in it which are
	 * identical and adjusting amounts accordingly
	 *
	 * @return Compressed version of this inventory with a variable size
	 */
	public Inventory compress() {
		List<ItemStack> stacks = new LinkedList<ItemStack>();
		for (int i = 0; i < getSize(); i++) {
			boolean found = false;
			ItemStack current = slots[i];
			if (current == null || current.isEmpty()) {
				continue;
			}
			for (ItemStack is : stacks) {
				if (is.equals(current)) {
					is.setAmount(is.getAmount() + current.getAmount());
					found = true;
				}
			}
			if (!found) {
				stacks.add(current.clone());
			}
		}
		Inventory inv = new DummyInventory(stacks.size());
		for (int i = 0; i < stacks.size(); i++) {
			inv.updateSlot(i, stacks.get(i));
		}
		return inv;
	}

	/**
	 * @return The hotbar slots in this inventory
	 */
	public DummyInventory getHotbar() {
		return new DummyInventory(Arrays.copyOfRange(slots, getPlayerHotbarStartingSlot(), getSize()));
	}

	/**
	 * @return The player storage slots, so the 27 normal inventory slots + the
	 *         hotbar
	 */
	public DummyInventory getPlayerStorage() {
		return new DummyInventory(Arrays.copyOfRange(slots, getPlayerStorageStartingSlot(), getSize()));
	}

	/**
	 * @return The player storage slots, so the 27 normal inventory slots, but not
	 *         the hotbar
	 */
	public DummyInventory getPlayerStorageWithoutHotbar() {
		return new DummyInventory(
				Arrays.copyOfRange(slots, getPlayerStorageStartingSlot(), getPlayerHotbarStartingSlot()));
	}

	/**
	 * Translates a slot in the storage inventory to an absolute slot in the
	 * inventory
	 *
	 * @param slot Relative slot in the storage section
	 * @return Absolute slot
	 */
	public short translateStorageSlotToTotal(int slot) {
		return (short) (slot + getPlayerStorageStartingSlot());
	}

	/**
	 * Translates a slot in the hotbar of the inventory to an absolute slot in the
	 * inventory
	 *
	 * @param slot Relative slot in the hotbar section
	 * @return Absolute slot
	 */
	public short translateHotbarToTotal(int slot) {
		return (short) (slot + getPlayerHotbarStartingSlot());
	}

	protected abstract int getPlayerStorageStartingSlot();

	protected int getPlayerHotbarStartingSlot() {
		return getPlayerStorageStartingSlot() + 27;
	}

	public static void updateCursor(ItemStack is) {
		cursor = is;
	}

	public static ItemStack getCursor() {
		return cursor;
	}

	@Override
	public Iterator<ItemStack> iterator() {
		return new Iterator<ItemStack>() {

			private int i = 0;

			@Override
			public boolean hasNext() {
				return i < (slots.length);
			}

			@Override
			public ItemStack next() {
				return slots[i++];
			}
		};
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ItemStack is : this) {
			if (is != null) {
				sb.append(is.toString() + ", ");
			}
		}
		return sb.toString();
	}
}
