package com.github.maxopoly.angeliacore.libs.packetEncoding;

public class ReadOnlyPacket extends BinaryReadOnlyData {

	public ReadOnlyPacket(byte[] data) {
		super(data);
		// first byte is the packet id, so skip that
		this.dataPointer = 1;
	}

	/**
	 *
	 * @return Packet id of the data (the first byte)
	 */
	public byte getPacketID() {
		return data[0];
	}
}
