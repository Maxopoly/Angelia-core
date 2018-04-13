package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.binary.WriteOnlyPacket;

import java.io.IOException;

public class KeepAlivePacket extends WriteOnlyPacket {

	public KeepAlivePacket(long randomNumber) throws IOException {
		super(0x0B);
		writeLong(randomNumber);
	}

}
