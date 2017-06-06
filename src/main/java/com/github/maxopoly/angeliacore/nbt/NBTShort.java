package com.github.maxopoly.angeliacore.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class NBTShort extends NBTElement {

	public static final byte ID = 2;

	private short value;

	public NBTShort(String name, short value) {
		super(name);
		this.value = value;
	}

	public short getValue() {
		return value;
	}

	@Override
	public byte[] serializeContent() {
		byte[] bytes = new byte[2];
		ByteBuffer.wrap(bytes).order(ByteOrder.BIG_ENDIAN).putShort(value);
		return bytes;
	}

	@Override
	public byte getID() {
		return ID;
	}

}
