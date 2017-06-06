package com.github.maxopoly.angeliacore.nbt;

public abstract class NBTElement {

	protected String name;

	public NBTElement(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	protected byte[] serializingPrefix() {
		if (name == null) {
			// in a list so no name and no tag
			return new byte[0];
		}
		byte[] nameSerialized = name.getBytes();
		short length = (short) nameSerialized.length;
		byte[] res = new byte[length + 3];
		res[1] = (byte) (length & 0xff);
		res[0] = (byte) ((length >> 8) & 0xff);
		res[3] = getID();
		for (int i = 0; i < length; i++) {
			res[i + 3] = nameSerialized[i];
		}
		return res;
	}

	public abstract byte[] serializeContent();

	public abstract byte getID();

}
