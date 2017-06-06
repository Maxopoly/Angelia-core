package com.github.maxopoly.angeliacore.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NBTIntArray extends NBTElement {

	public static final byte ID = 11;

	private int[] value;

	public NBTIntArray(String name, int[] value) {
		super(name);
		this.value = value;
	}

	public int[] getValue() {
		return value;
	}

	public int getLength() {
		return value.length;
	}

	@Override
	public byte[] serializeContent() {
		byte[] bytes = new byte[(value.length + 1) * 4];
		ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).putInt(value.length);
		for (int val : value) {
			buffer.putInt(val);
		}
		return bytes;
	}

	@Override
	public byte getID() {
		return ID;
	}

}
