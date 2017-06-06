package com.github.maxopoly.angeliacore.packet;

import com.github.maxopoly.angeliacore.model.ItemStack;
import com.github.maxopoly.angeliacore.nbt.NBTCompound;
import com.github.maxopoly.angeliacore.nbt.NBTParser;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class ReadOnlyPacket {

	private byte[] data;
	private int dataPointer;
	private byte packetID;

	public ReadOnlyPacket(byte[] data) {
		this.data = data;
		this.packetID = data[0];
		this.dataPointer = 1;
	}

	public byte getPacketID() {
		return packetID;
	}

	public long readLong() throws EndOfPacketException {
		if (dataPointer + 8 > data.length) {
			throw new EndOfPacketException("Tried to read long, but data pointer was at " + dataPointer + " out of "
					+ data.length);
		}
		long value = 0;
		for (int i = 0; i < 8; i++) {
			value = (value << 8) + (((long) data[i + dataPointer]) & (long) 0xff);
		}
		dataPointer += 8;
		return value;
	}

	public short readSignedShort() throws EndOfPacketException {
		if (dataPointer + 2 > data.length) {
			throw new EndOfPacketException("Tried to read short, but data pointer was at " + dataPointer + " out of "
					+ data.length);
		}
		byte[] intData = Arrays.copyOfRange(data, dataPointer, dataPointer + 2);
		short result = ByteBuffer.wrap(intData).order(ByteOrder.BIG_ENDIAN).getShort();
		dataPointer += 2;
		return result;
	}

	public double readDouble() throws EndOfPacketException {
		if (dataPointer + 8 > data.length) {
			throw new EndOfPacketException("Tried to read double, but data pointer was at " + dataPointer + " out of "
					+ data.length);
		}
		byte[] doubleData = Arrays.copyOfRange(data, dataPointer, dataPointer + 8);
		double result = ByteBuffer.wrap(doubleData).order(ByteOrder.BIG_ENDIAN).getDouble();
		dataPointer += 8;
		return result;
	}

	public float readFloat() throws EndOfPacketException {
		if (dataPointer + 4 > data.length) {
			throw new EndOfPacketException("Tried to read float, but data pointer was at " + dataPointer + " out of "
					+ data.length);
		}
		byte[] doubleData = Arrays.copyOfRange(data, dataPointer, dataPointer + 4);
		float result = ByteBuffer.wrap(doubleData).order(ByteOrder.BIG_ENDIAN).getFloat();
		dataPointer += 4;
		return result;
	}

	public byte readUnsignedByte() throws EndOfPacketException {
		if (dataPointer + 1 > data.length) {
			throw new EndOfPacketException("Tried to read byte, but data pointer was at " + dataPointer + " out of "
					+ data.length);
		}
		return data[dataPointer++];
	}

	public byte[] readBytes(int amount) throws EndOfPacketException {
		if (dataPointer + amount > data.length) {
			throw new EndOfPacketException("Tried to read " + amount + " bytes, but data pointer was at " + dataPointer
					+ " out of " + data.length);
		}
		byte[] resultData = new byte[amount];
		for (int i = 0; i < amount; i++) {
			resultData[i] = data[i + dataPointer];
		}
		dataPointer += amount;
		return resultData;
	}

	public ItemStack readItemStack() throws EndOfPacketException {
		short id = readSignedShort();
		if (id == -1) {
			return new ItemStack(id);
		}
		byte count = readUnsignedByte();
		short dmg = readSignedShort();
		NBTParser parser = new NBTParser(Arrays.copyOfRange(data, dataPointer, data.length));
		NBTCompound compound = parser.parse();
		dataPointer += parser.getLength();
		return new ItemStack(id, count, dmg, compound);
	}

	public byte[] readByteArray() throws EndOfPacketException {
		return readBytes(readVarInt());
	}

	/**
	 * Reads a single string from the input data. Strings are made up of an initial varint denoting the length of the
	 * string and the actual string encoded in utf-8
	 * 
	 * @return String read
	 * @throws EndOfPacketException
	 *           Thrown if packet ended before String ended
	 */
	public String readString() throws EndOfPacketException {
		int charAmount = readVarInt();
		return new String(readBytes(charAmount));
	}

	public int readSignedInt() throws EndOfPacketException {
		if (dataPointer + 4 > data.length) {
			throw new EndOfPacketException("Tried to read int, but data pointer was at " + dataPointer + " out of "
					+ data.length);
		}
		byte[] intData = Arrays.copyOfRange(data, dataPointer, dataPointer + 4);
		int result = ByteBuffer.wrap(intData).order(ByteOrder.BIG_ENDIAN).getInt();
		dataPointer += 4;
		return result;
	}

	public boolean readBoolean() throws EndOfPacketException {
		byte b = readUnsignedByte();
		if (b == 0) {
			return false;
		}
		return true;
	}

	/**
	 * Reads a varInt from the input data
	 * 
	 * @return varInt read
	 * @throws IOException
	 *           thrown if varInt could not be read or was too big
	 */
	public int readVarInt() {
		int i = 0;
		int j = 0;
		while (true) {
			int k = data[dataPointer++];
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5)
				throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128)
				break;
		}
		return i;
	}

}
