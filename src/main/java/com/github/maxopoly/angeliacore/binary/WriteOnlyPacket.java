package com.github.maxopoly.angeliacore.binary;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public class WriteOnlyPacket {

	private ByteArrayOutputStream buffer;
	private DataOutputStream dataStream;

	public WriteOnlyPacket(int id) {
		this();
		try {
			dataStream.writeByte(id); // packet id
		} catch (IOException e) {
			System.out.println("Failed to create packet with id " + id);
			e.printStackTrace();
		}
	}

	public WriteOnlyPacket() {
		buffer = new ByteArrayOutputStream();
		dataStream = new DataOutputStream(buffer);
	}

	public void writeString(String string) throws IOException {
		byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
		writeVarInt(bytes.length);
		dataStream.write(bytes);
	}

	public void writeVarInt(int paramInt) throws IOException {
		while (true) {
			if ((paramInt & 0xFFFFFF80) == 0) {
				dataStream.writeByte(paramInt);
				return;
			}

			dataStream.writeByte(paramInt & 0x7F | 0x80);
			paramInt >>>= 7;
		}
	}

	public void writeByte(byte b) throws IOException {
		dataStream.writeByte(b);
	}

	public void writeShort(short s) throws IOException {
		dataStream.writeShort(s);
	}

	public void writeInt(int i) throws IOException {
		dataStream.writeInt(i);
	}

	public void writeBytes(byte[] bytes) throws IOException {
		dataStream.write(bytes);
	}

	public void writeLong(long l) throws IOException {
		dataStream.writeLong(l);
	}

	public void writeDouble(double d) throws IOException {
		byte[] bytes = new byte[8];
		ByteBuffer.wrap(bytes).putDouble(d);
		writeBytes(bytes);
	}

	public void writeFloat(float f) throws IOException {
		byte[] bytes = new byte[4];
		ByteBuffer.wrap(bytes).putFloat(f);
		writeBytes(bytes);
	}

	public void writeByteArray(byte[] bytes) throws IOException {
		writeVarInt(bytes.length);
		writeBytes(bytes);
	}

	public void writeBoolean(boolean bool) throws IOException {
		byte toWrite = (byte) (bool ? 0x01 : 0x00);
		writeByte(toWrite);
	}

	public void writePosition(int x, int y, int z) throws IOException {
		long value = (((long) x & 0x3FFFFFF) << 38) | (((long) y & 0xFFF) << 26) | ((long) z & 0x3FFFFFF);
		writeLong(value);
	}

	public void writeItemStack(ItemStack is) throws IOException {
		short id = (short)is.getMaterial().getID();
		writeShort(id);
		if (id == -1) {
			return;
		}
		writeByte((byte) is.getAmount());
		writeShort(is.getDamage());
		if (is.getNBT() != null) {
			writeBytes(is.getNBT().serialize());
		} else {
			writeByte((byte) 0);
		}
	}

	public byte[] toByteArrayIncludingLength() throws IOException {
		byte[] data = toByteArray();
		WriteOnlyPacket tempPacket = new WriteOnlyPacket();
		tempPacket.writeVarInt(data.length);
		tempPacket.writeBytes(data);
		byte[] resData = tempPacket.toByteArray();
		return resData;
	}

	public byte[] toByteArray() throws IOException {
		byte[] data = buffer.toByteArray();
		dataStream.close();
		return data;
	}

	public int getSize() {
		return buffer.size();
	}
}
