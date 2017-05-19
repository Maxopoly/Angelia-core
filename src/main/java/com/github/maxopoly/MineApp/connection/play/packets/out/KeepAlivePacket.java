package com.github.maxopoly.MineApp.connection.play.packets.out;

import com.github.maxopoly.MineApp.packet.WriteOnlyPacket;
import java.io.IOException;

public class KeepAlivePacket extends WriteOnlyPacket {

	public KeepAlivePacket(int randomNumber) throws IOException {
		super(0x0B);
		writeVarInt(randomNumber);
	}

}
