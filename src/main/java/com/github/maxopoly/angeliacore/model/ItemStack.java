package com.github.maxopoly.angeliacore.model;

import com.github.maxopoly.angeliacore.nbt.NBTCompound;

public class ItemStack {

	private short id;
	private byte amount;
	private short damage;
	private NBTCompound tag;

	public ItemStack(short id, byte amount, short damage, NBTCompound tag) {
		this.id = id;
		this.amount = amount;
		this.damage = damage;
	}

	public ItemStack(short id, byte amount, short damage) {
		this(id, amount, damage, null);
	}

	public ItemStack(short id, byte amount) {
		this(id, amount, (short) 0, null);
	}

	public ItemStack(short id) {
		this(id, (byte) 1, (short) 0, null);
	}

	public short getID() {
		return id;
	}

	public byte getAmount() {
		return amount;
	}

	public short getDamage() {
		return damage;
	}

	public NBTCompound getNBT() {
		return tag;
	}

	/**
	 * @return Whether this instance represents an empty item slot
	 */
	public boolean isEmpty() {
		return id == -1;
	}

	@Override
	public String toString() {
		return amount + " " + id + ":" + damage;
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
		return is.id == id && is.damage == damage && tag.equals(is.tag);
	}

}
