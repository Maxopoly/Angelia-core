package com.github.maxopoly.angeliacore.libs.nbt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class NBTList<E extends NBTElement> extends NBTElement implements Iterable<E> {

	public static final byte ID = 9;

	private List<E> elements;
	private byte elementID;

	public NBTList(String name, byte type) {
		super(name);
		this.elements = new LinkedList<E>();
		this.elementID = type;
	}

	public void add(E element) {
		elements.add(element);
	}

	public E getElement(int index) {
		return elements.get(index);
	}

	public int getLength() {
		return elements.size();
	}

	@Override
	public Iterator<E> iterator() {
		return elements.iterator();
	}

	public Object getValue() {
		return elements;
	}

	@Override
	public byte[] serializeContent() {
		// to avoid copying everything around often we first collect results in a list and only concatenate them once
		List<byte[]> tempEle = new LinkedList<byte[]>();
		int length = 0;
		for (NBTElement element : elements) {
			byte[] temp = element.serializeContent();
			tempEle.add(temp);
			length += temp.length;
		}
		byte[] listLength = new byte[4];
		ByteBuffer.wrap(listLength).order(ByteOrder.BIG_ENDIAN).putInt(elements.size());
		byte[] res = new byte[length + 5];
		res[0] = elementID;
		for (int i = 1; i < 5; i++) {
			res[i] = listLength[i];
		}
		int index = 5;
		for (byte[] curr : tempEle) {
			for (int i = 0; i < curr.length; i++) {
				res[i + index] = curr[i];
			}
		}
		return res;
	}

	@Override
	public byte getID() {
		return ID;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NBTList)) {
			return false;
		}
		NBTList<E> other;
		try {
			other = (NBTList<E>) o;
		} catch (ClassCastException e) {
			// if anyone actually knows how to check whether generics are equal let me now, because I dont
			return false;
		}
		if (getLength() != other.getLength()) {
			return false;
		}
		for (int i = 0; i < getLength(); i++) {
			if (!other.getElement(i).equals(getElement(i))) {
				return false;
			}
		}
		return true;
	}

	@Override
	public NBTElement clone() {
		NBTList<E> list = new NBTList<E>(name, elementID);
		for (E e : elements) {
			list.add((E) e.clone());
		}
		return list;
	}
}
