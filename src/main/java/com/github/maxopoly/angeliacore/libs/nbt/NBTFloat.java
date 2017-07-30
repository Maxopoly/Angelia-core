package com.github.maxopoly.angeliacore.libs.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NBTFloat extends NBTElement {

	public static final byte ID = 5;

	private float value;

	public NBTFloat(String name, float value) {
		super(name);
		this.value = value;
	}

	public float getValue() {
		return value;
	}

	@Override
	public byte[] serializeContent() {
		byte[] bytes = new byte[4];
		ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).putFloat(value);
		return bytes;
	}

	@Override
	public byte getID() {
		return ID;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTFloat && ((NBTFloat) o).value == value;
	}

	@Override
	public NBTElement clone() {
		return new NBTFloat(name, value);
	}

}
