package com.github.maxopoly.angeliacore.libs.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

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

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTIntArray && Arrays.equals(((NBTIntArray) o).value, value);
	}

	@Override
	public NBTElement clone() {
		return new NBTIntArray(name, Arrays.copyOf(value, value.length));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < value.length; i++) {
			sb.append(" ");
			sb.append(value [i]);
		}
		sb.append(" ");

		return String.format("[%s]", sb.toString());
	}

	@Override
	public String getTypeName() {
		return "int array";
	}
}
