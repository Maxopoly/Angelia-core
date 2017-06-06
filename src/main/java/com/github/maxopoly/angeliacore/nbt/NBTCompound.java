package com.github.maxopoly.angeliacore.nbt;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

public class NBTCompound extends NBTElement {

	private Map<String, NBTElement> content;

	public static final byte COMPOUND_START_ID = 10;
	public static final byte COMPOUND_END_ID = 0;

	public NBTCompound(String name) {
		super(name);
		this.content = new HashMap<String, NBTElement>();
	}

	public void add(NBTElement element) {
		if (element.getName() == null) {
			throw new IllegalArgumentException("Can't add unnamed tags to compounds");
		}
		content.put(element.getName(), element);
	}

	public NBTElement getElement(String name) {
		return content.get(name);
	}

	@Override
	public byte[] serializeContent() {
		// to avoid copying everything around often we first collect results in a list and only concatenate them once
		List<byte[]> elements = new LinkedList<byte[]>();
		int length = 0;
		for (NBTElement element : content.values()) {
			byte[] temp = ArrayUtils.addAll(element.serializingPrefix(), element.serializeContent());
			elements.add(temp);
			length += temp.length;
		}
		byte[] res = new byte[length + 1];
		int index = 0;
		for (byte[] curr : elements) {
			for (int i = 0; i < curr.length; i++) {
				res[i + index] = curr[i];
			}
		}
		// end tag
		res[res.length - 1] = 0;
		return res;
	}

	public byte[] serialize() {
		return ArrayUtils.addAll(serializingPrefix(), serializeContent());
	}

	@Override
	public byte getID() {
		return COMPOUND_START_ID;
	}

	@Override
	public boolean equals(Object o) {
		return o instanceof NBTCompound && ((NBTCompound) o).content.equals(content);
	}

}
