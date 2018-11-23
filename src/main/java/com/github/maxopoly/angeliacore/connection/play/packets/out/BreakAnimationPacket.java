package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class BreakAnimationPacket extends WriteOnlyPacket {

	public BreakAnimationPacket() throws IOException {
		super(0x1D);
		writeVarInt(0);
	}
}
