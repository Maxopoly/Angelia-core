package com.github.maxopoly.angeliacore.libs.nbt;

public class NBTByte extends NBTElement {

	public static final byte ID = 1;

	private byte value;

	public NBTByte(String name, byte value) {
		super(name);
		this.value = value;
	}

	@Override
	public NBTElement clone() {
		return new NBTByte(name, value);
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTByte && ((NBTByte) o).value == value;
	}

	@Override
	public byte getID() {
		return ID;
	}

	@Override
	public String getTypeName() {
		return "byte";
	}

	public byte getValue() {
		return value;
	}

	@Override
	public byte[] serializeContent() {
		return new byte[] { value };
	}

	@Override
	public String toString() {
		return "" + value;
	}
}
