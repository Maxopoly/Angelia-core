package com.github.maxopoly.angeliacore.libs.nbt;

import com.github.maxopoly.angeliacore.binary.BinaryReadOnlyData;
import com.github.maxopoly.angeliacore.binary.EndOfPacketException;

public class NBTParser extends BinaryReadOnlyData {

	public NBTParser(byte[] data) {
		super(data);
	}

	public NBTCompound parse() throws IllegalArgumentException, EndOfPacketException {
		byte startingByte = readByte();
		if (startingByte == 0) {
			// null tag
			return null;
		}
		if (startingByte != NBTCompound.COMPOUND_START_ID) {
			throw new IllegalArgumentException("Compound started with invalid id " + startingByte);
		}
		return parseNBTCompound(false);
	}

	public int getLength() {
		return dataPointer;
	}

	private NBTCompound parseNBTCompound(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
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
					foundElement = parseNBTCompound(false);
					break;
				case NBTIntArray.ID:
					foundElement = parseNBTIntArray(false);
					break;
				default:
					throw new IllegalArgumentException("NBT Type id " + tagID + " is not valid, nbt so far: " + compound.toString());
			}
			if (foundElement != null) {
				compound.add(foundElement);
			}
		}
		System.out.println("Parsed comp: " + compound.toString()) ;
		return compound;
	}

	private NBTByte parseNBTByte(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		byte b = readByte();
		return new NBTByte(name, b);
	}

	private NBTShort parseNBTShort(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		short s = readShort();
		return new NBTShort(name, s);
	}

	private NBTInt parseNBTInt(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		int s = readInt();
		return new NBTInt(name, s);
	}

	private NBTLong parseNBTLong(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		long b = readLong();
		return new NBTLong(name, b);
	}

	private NBTFloat parseNBTFloat(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		float b = readFloat();
		return new NBTFloat(name, b);
	}

	private NBTDouble parseNBTDouble(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		double b = readDouble();
		return new NBTDouble(name, b);
	}

	private NBTByteArray parseNBTByteArray(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		int length = readInt();
		byte[] bArray = readBytes(length);
		return new NBTByteArray(name, bArray);
	}

	private NBTString parseNBTString(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		String value = readString(2);
		return new NBTString(name, value);
	}

	private NBTIntArray parseNBTIntArray(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		int length = readInt();
		int[] iArray = new int[length];
		for (int i = 0; i < length; i++) {
			iArray[i] = readInt();
		}
		return new NBTIntArray(name, iArray);
	}

	private NBTList parseNBTList(boolean inList) throws EndOfPacketException {
		String name = inList ? null : readString(2);
		byte typeID = readByte();
		NBTList<NBTElement> list = new NBTList<NBTElement>(name, typeID);
		int length = readInt();
		System.out.println("Parsed length: " + length);
		if (length < 0) {
			length = 0;
		}
		for (int i = 0; i < length; i++) {
			NBTElement parsedElement = null;

			switch (typeID) {
				case NBTCompound.COMPOUND_END_ID:
					if (length > 0) {
						throw new IllegalArgumentException("Compound end tag is not allowed as type of lists longer than 0");
					}
					break;
				case NBTByte.ID:
					parsedElement = parseNBTByte(true);
					break;
				case NBTShort.ID:
					parsedElement = parseNBTShort(true);
					break;
				case NBTInt.ID:
					parsedElement = parseNBTInt(true);
					break;
				case NBTLong.ID:
					parsedElement = parseNBTLong(true);
					break;
				case NBTFloat.ID:
					parsedElement = parseNBTFloat(true);
					break;
				case NBTDouble.ID:
					parsedElement = parseNBTDouble(true);
					break;
				case NBTByteArray.ID:
					parsedElement = parseNBTByteArray(true);
					break;
				case NBTString.ID:
					parsedElement = parseNBTString(true);
					break;
				case NBTList.ID:
					parsedElement = parseNBTList(true);
					break;
				case NBTCompound.COMPOUND_START_ID:
					parsedElement = parseNBTCompound(true);
					break;
				case NBTIntArray.ID:
					parsedElement = parseNBTIntArray(true);
					break;
				default:
					throw new IllegalArgumentException("NBT Type id " + typeID + " is not valid");
			}
			if (parsedElement != null) {
				list.add(parsedElement);
			}
		}
		return list;
	}
}
