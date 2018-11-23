package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class HeldItemChangePacket extends WriteOnlyPacket {

	public HeldItemChangePacket(int slot) throws IOException {
		super(0x1A);
		writeShort((short) slot);
	}

}
