package com.github.maxopoly.angeliacore.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NBTInt extends NBTElement {

	public static final byte ID = 3;

	private int value;

	public NBTInt(String name, int value) {
		super(name);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	@Override
	public byte[] serializeContent() {
		byte[] bytes = new byte[4];
		ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).putInt(value);
		return bytes;
	}

	@Override
	public byte getID() {
		return ID;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTInt && ((NBTInt) o).value == value;
	}

	@Override
	public NBTElement clone() {
		return new NBTInt(name, value);
	}

}
