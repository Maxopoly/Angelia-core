package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;

import java.io.IOException;

public class KeepAlivePacket extends WriteOnlyPacket {

	public KeepAlivePacket(int randomNumber) throws IOException {
		super(0x0B);
		writeVarInt(randomNumber);
	}

}
