package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class CloseWindowPacket extends WriteOnlyPacket {

	public CloseWindowPacket(byte windowID) throws IOException {
		super(0x08);
		writeByte(windowID);
	}
}
