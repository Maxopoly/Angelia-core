package com.github.maxopoly.angeliacore.nbt;

public class NBTByte extends NBTElement {

	public static final byte ID = 1;

	private byte value;

	public NBTByte(String name, byte value) {
		super(name);
		this.value = value;
	}

	public byte getValue() {
		return value;
	}

	@Override
	public byte[] serializeContent() {
		return new byte[] { value };
	}

	@Override
	public byte getID() {
		return ID;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTByte && ((NBTByte) o).value == value;
	}

}
