package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class ChatPacket extends WriteOnlyPacket {

	public ChatPacket(String msg) throws IOException {
		super(0x02);
		writeString(msg);
	}

}
