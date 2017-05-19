package com.github.maxopoly.MineApp.connection.play.packets.out;

import com.github.maxopoly.MineApp.packet.WriteOnlyPacket;
import java.io.IOException;

public class ChatPacket extends WriteOnlyPacket {

	public ChatPacket(String msg) throws IOException {
		super(0x02);
		writeString(msg);
	}

}
