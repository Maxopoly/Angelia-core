package com.github.maxopoly.angeliacore.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class NBTByteArray extends NBTElement {

	public static final byte ID = 7;

	private byte[] value;

	public NBTByteArray(String name, byte[] value) {
		super(name);
		this.value = value;
	}

	public byte[] getValue() {
		return value;
	}

	public int getLength() {
		return value.length;
	}

	@Override
	public byte[] serializeContent() {
		byte[] res = new byte[value.length + 4];
		int length = value.length;
		byte[] bytes = new byte[4];
		ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).putInt(length);
		for (int i = 0; i < 4; i++) {
			res[i] = bytes[i];
		}
		for (int i = 0; i < length; i++) {
			res[i + 3] = value[i];
		}
		return res;
	}

	@Override
	public byte getID() {
		return ID;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTByteArray && Arrays.equals(((NBTByteArray) o).value, value);
	}
}
