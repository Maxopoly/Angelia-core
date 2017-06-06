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

}
