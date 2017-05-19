package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;

import java.io.IOException;

public class ChatPacket extends WriteOnlyPacket {

	public ChatPacket(String msg) throws IOException {
		super(0x02);
		writeString(msg);
	}

}
