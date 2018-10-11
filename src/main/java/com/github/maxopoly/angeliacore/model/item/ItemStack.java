package com.github.maxopoly.angeliacore.model.item;

import com.github.maxopoly.angeliacore.libs.nbt.NBTCompound;
import com.github.maxopoly.angeliacore.libs.nbt.NBTElement;
import com.github.maxopoly.angeliacore.libs.nbt.NBTList;
import com.github.maxopoly.angeliacore.libs.nbt.NBTShort;

import java.util.HashMap;
import java.util.Map;

public final class ItemStack {

	private Material material;
	private int amount;
	private short damage;
	private NBTCompound tag;

	public ItemStack(Material mat, int amount, short damage, NBTCompound tag) {
		this.material = mat;
		this.amount = amount;
		this.damage = damage;
		this.tag = tag;
	}

	public ItemStack(Material mat, byte amount, short damage) {
		this(mat, amount, damage, null);
	}

	public ItemStack(Material mat, byte amount) {
		this(mat, amount, (short) 0, null);
	}

	public ItemStack(Material mat) {
		this(mat, (byte) 1, (short) 0, null);
	}

	public Material getMaterial() {
		return material;
	}

	/**
	 * How many items this stack has. Minecraft models this as a byte, but we use an int so we can have bigger stacks
	 * clientside shenanigans
	 * 
	 * @return Amount of items in this stack
	 */
	public int getAmount() {
		return amount;
	}

	public short getDamage() {
		return damage;
	}

	public NBTCompound getNBT() {
		return tag;
	}

	public boolean isEnchanted() {
		return getEnchants().size() != 0;
	}

	public Map<Enchantment, Integer> getEnchants() {
		Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
		if (tag == null) {
			return enchants;
		}
		NBTElement list = tag.getElement("ench");
		if (list == null) {
			return enchants;
		}
		if (!(list instanceof NBTList)) {
			return enchants;
		}
		@SuppressWarnings("unchecked")
		NBTList<NBTCompound> enchantList = (NBTList<NBTCompound>) list;
		for (NBTCompound comp : enchantList) {
			short id = ((NBTShort) comp.getElement("id")).getValue();
			short lvl = ((NBTShort) comp.getElement("lvl")).getValue();
			Enchantment ench = Enchantment.fromID(id);
			enchants.put(ench, (int) lvl);
		}
		return enchants;

	}

	/**
	 * Sets the amount of items in this stack
	 * 
	 * @param amount
	 *          New amount
	 */
	public void setAmount(int amount) {
		this.amount = amount;
	}

	/**
	 * @return Whether this instance represents an empty item slot
	 */
	public boolean isEmpty() {
		return material == Material.EMPTY_SLOT;
	}

	@Override
	public String toString() {
		return amount + " " + material.name() + ":" + damage;
	}

	@Override
	public ItemStack clone() {
		return new ItemStack(material, amount, damage, tag != null ? (NBTCompound) tag.clone() : null);
	}

	/**
	 * Checks whether the type of item is equals and ignores the amount
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof ItemStack)) {
			return false;
		}
		ItemStack is = (ItemStack) o;
		return is.material == material && is.damage == damage
				&& ((tag == null && is.tag == null) || (tag != null && is.tag != null && tag.equals(is.tag)));
	}
}
