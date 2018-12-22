package com.github.maxopoly.angeliacore.libs.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NBTDouble extends NBTElement {

	public static final byte ID = 6;

	private double value;

	public NBTDouble(String name, double value) {
		super(name);
		this.value = value;
	}

	@Override
	public NBTElement clone() {
		return new NBTDouble(name, value);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTDouble && ((NBTDouble) o).value == value;
	}

	@Override
	public byte getID() {
		return ID;
	}

	@Override
	public String getTypeName() {
		return "double";
	}

	public double getValue() {
		return value;
	}

	@Override
	public byte[] serializeContent() {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).putDouble(value);
		return bytes;
	}

	@Override
	public String toString() {
		return "" + value;
	}
}
