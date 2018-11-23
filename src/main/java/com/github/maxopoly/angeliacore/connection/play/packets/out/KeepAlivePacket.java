package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class KeepAlivePacket extends WriteOnlyPacket {

	public KeepAlivePacket(long randomNumber) throws IOException {
		super(0x0B);
		writeLong(randomNumber);
	}

}
