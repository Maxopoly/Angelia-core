package com.github.maxopoly.angeliacore.libs.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NBTString extends NBTElement {

	public static final byte ID = 8;

	private String value;

	public NBTString(String name, String value) {
		super(name);
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public byte[] serializeContent() {
		byte[] converted = value.getBytes();
		byte[] res = new byte[converted.length + 2];
		ByteBuffer.wrap(res).order(ByteOrder.BIG_ENDIAN).putShort((short) value.length());
		for (int i = 0; i < converted.length; i++) {
			res[i + 2] = converted[i];
		}
		return res;
	}

	@Override
	public byte getID() {
		return ID;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTString && ((NBTString) o).value.equals(value);
	}

	@Override
	public NBTElement clone() {
		return new NBTString(name, value);
	}
}
