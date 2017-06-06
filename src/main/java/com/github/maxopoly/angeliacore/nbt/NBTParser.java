package com.github.maxopoly.angeliacore.nbt;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class NBTParser {

	private int dataPointer;
	private byte[] data;

	public NBTParser(byte[] data) {
		this.data = data;
		this.dataPointer = 0;
	}

	public NBTCompound parse() throws IllegalArgumentException {
		byte startingByte = readByte();
		if (startingByte == 0) {
			// null tag
			return null;
		}
		if (startingByte != NBTCompound.COMPOUND_START_ID) {
			throw new IllegalArgumentException("Compound started with invalid id " + startingByte);
		}
		return parseCompound(false);
	}

	public int getLength() {
		return dataPointer;
	}

	private NBTCompound parseCompound(boolean inList) {
		String name = inList ? null : readString();
		NBTCompound compound = new NBTCompound(name);
		boolean endFound = false;
		while (!endFound) {
			byte tagID = readByte();
			NBTElement foundElement = null;
			switch (tagID) {
				case NBTCompound.COMPOUND_END_ID:
					endFound = true;
					break;
				case NBTByte.ID:
					foundElement = parseNBTByte(false);
					break;
				case NBTShort.ID:
					foundElement = parseNBTShort(false);
					break;
				case NBTInt.ID:
					foundElement = parseNBTInt(false);
					break;
				case NBTLong.ID:
					foundElement = parseNBTLong(false);
					break;
				case NBTFloat.ID:
					foundElement = parseNBTFloat(false);
					break;
				case NBTDouble.ID:
					foundElement = parseNBTDouble(false);
					break;
				case NBTByteArray.ID:
					foundElement = parseNBTByteArray(false);
					break;
				case NBTString.ID:
					foundElement = parseNBTString(false);
					break;
				case NBTList.ID:
					foundElement = parseNBTList(false);
					break;
				case NBTCompound.COMPOUND_START_ID:
					foundElement = parseCompound(false);
					break;
				case NBTIntArray.ID:
					foundElement = parseNBTIntArray(false);
					break;
				default:
					throw new IllegalArgumentException("NBT Type id " + tagID + " is not valid");
			}
			if (foundElement != null) {
				compound.add(foundElement);
			}
		}
		return compound;
	}

	private NBTByte parseNBTByte(boolean inList) {
		String name = inList ? null : readString();
		byte b = readByte();
		return new NBTByte(name, b);
	}

	private NBTShort parseNBTShort(boolean inList) {
		String name = inList ? null : readString();
		short s = readSignedShort();
		return new NBTShort(name, s);
	}

	private NBTInt parseNBTInt(boolean inList) {
		String name = inList ? null : readString();
		int s = readSignedInt();
		return new NBTInt(name, s);
	}

	private NBTLong parseNBTLong(boolean inList) {
		String name = inList ? null : readString();
		long b = readSignedLong();
		return new NBTLong(name, b);
	}

	private NBTFloat parseNBTFloat(boolean inList) {
		String name = inList ? null : readString();
		float b = readFloat();
		return new NBTFloat(name, b);
	}

	private NBTDouble parseNBTDouble(boolean inList) {
		String name = inList ? null : readString();
		double b = readDouble();
		return new NBTDouble(name, b);
	}

	private NBTByteArray parseNBTByteArray(boolean inList) {
		String name = inList ? null : readString();
		int length = readSignedInt();
		byte[] bArray = readBytes(length);
		return new NBTByteArray(name, bArray);
	}

	private NBTString parseNBTString(boolean inList) {
		String name = inList ? null : readString();
		String value = readString();
		return new NBTString(name, value);
	}

	private NBTIntArray parseNBTIntArray(boolean inList) {
		String name = inList ? null : readString();
		int length = readSignedInt();
		int[] iArray = new int[length];
		for (int i = 0; i < length; i++) {
			iArray[i] = readSignedInt();
		}
		return new NBTIntArray(name, iArray);
	}

	private NBTList parseNBTList(boolean inList) {
		String name = inList ? null : readString();
		byte typeID = readByte();
		NBTList<NBTElement> list = new NBTList<NBTElement>(name, typeID);
		int length = readSignedInt();
		if (length < 0) {
			length = 0;
		}
		Method parsingMethod = null;
		try {
			switch (typeID) {
				case NBTCompound.COMPOUND_END_ID:
					if (length > 0) {
						throw new IllegalArgumentException("Compound end tag is not allowed as type of lists longer than 0");
					}
					break;
				case NBTByte.ID:
					parsingMethod = this.getClass().getMethod("parseNBTByte", boolean.class);
					break;
				case NBTShort.ID:
					parsingMethod = this.getClass().getMethod("parseNBTShort", boolean.class);
					break;
				case NBTInt.ID:
					parsingMethod = this.getClass().getMethod("parseNBTInt", boolean.class);
					break;
				case NBTLong.ID:
					parsingMethod = this.getClass().getMethod("parseNBTLong", boolean.class);
					break;
				case NBTFloat.ID:
					parsingMethod = this.getClass().getMethod("parseNBTFloat", boolean.class);
					break;
				case NBTDouble.ID:
					parsingMethod = this.getClass().getMethod("parseNBTDouble", boolean.class);
					break;
				case NBTByteArray.ID:
					parsingMethod = this.getClass().getMethod("parseNBTByteArray", boolean.class);
					break;
				case NBTString.ID:
					parsingMethod = this.getClass().getMethod("parseNBTString", boolean.class);
					break;
				case NBTList.ID:
					parsingMethod = this.getClass().getMethod("parseNBTList", boolean.class);
					break;
				case NBTCompound.COMPOUND_START_ID:
					parsingMethod = this.getClass().getMethod("parseCompound", boolean.class);
					break;
				case NBTIntArray.ID:
					parsingMethod = this.getClass().getMethod("parseNBTIntArray", boolean.class);
					break;
				default:
					throw new IllegalArgumentException("NBT Type id " + typeID + " is not valid");
			}
		} catch (NoSuchMethodException e) {
			// should never happen
			e.printStackTrace();
		}
		for (int i = 0; i < length; i++) {
			NBTElement parsedElement = null;
			try {
				parsedElement = (NBTElement) parsingMethod.invoke(this, true);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			if (parsedElement != null) {
				list.add(parsedElement);
			}
		}
		return list;
	}

	private String readString() {
		short charAmount = readSignedShort();
		return new String(readBytes(charAmount));
	}

	private byte readByte() {
		return data[dataPointer++];
	}

	public byte[] readBytes(int amount) {
		byte[] resultData = new byte[amount];
		for (int i = 0; i < amount; i++) {
			resultData[i] = data[i + dataPointer];
		}
		dataPointer += amount;
		return resultData;
	}

	public long readSignedLong() {
		byte[] intData = Arrays.copyOfRange(data, dataPointer, dataPointer + 8);
		long result = ByteBuffer.wrap(intData).order(ByteOrder.BIG_ENDIAN).getLong();
		dataPointer += 8;
		return result;
	}

	public short readSignedShort() {
		byte[] intData = Arrays.copyOfRange(data, dataPointer, dataPointer + 2);
		short result = ByteBuffer.wrap(intData).order(ByteOrder.BIG_ENDIAN).getShort();
		dataPointer += 2;
		return result;
	}

	public double readDouble() {
		byte[] doubleData = Arrays.copyOfRange(data, dataPointer, dataPointer + 8);
		double result = ByteBuffer.wrap(doubleData).order(ByteOrder.BIG_ENDIAN).getDouble();
		dataPointer += 8;
		return result;
	}

	public int readSignedInt() {
		byte[] intData = Arrays.copyOfRange(data, dataPointer, dataPointer + 4);
		int result = ByteBuffer.wrap(intData).order(ByteOrder.BIG_ENDIAN).getInt();
		dataPointer += 4;
		return result;
	}

	public float readFloat() {
		byte[] doubleData = Arrays.copyOfRange(data, dataPointer, dataPointer + 4);
		float result = ByteBuffer.wrap(doubleData).order(ByteOrder.BIG_ENDIAN).getFloat();
		dataPointer += 4;
		return result;
	}

}
