package com.github.maxopoly.angeliacore.libs.nbt;

public abstract class NBTElement {

	protected String name;

	public NBTElement(String name) {
		this.name = name;
	}

	@Override
	public abstract NBTElement clone();

	@Override
	public abstract boolean equals(Object o);

	public abstract byte getID();

	public String getName() {
		return name;
	}

	public abstract String getTypeName();

	public abstract byte[] serializeContent();

	protected byte[] serializingPrefix() {
		if (name == null) {
			// in a list so no name and no tag
			return new byte[0];
		}
		byte[] nameSerialized = name.getBytes();
		short length = (short) nameSerialized.length;
		byte[] res = new byte[length + 3];
		res[2] = (byte) (length & 0xff);
		res[1] = (byte) ((length >> 8) & 0xff);
		res[0] = getID();
		for (int i = 0; i < length; i++) {
			res[i + 3] = nameSerialized[i];
		}
		return res;
	}

	@Override
	public abstract String toString();

}
