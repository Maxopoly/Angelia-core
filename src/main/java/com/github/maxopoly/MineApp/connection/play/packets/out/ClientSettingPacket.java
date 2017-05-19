package com.github.maxopoly.MineApp.connection.play.packets.out;

import com.github.maxopoly.MineApp.packet.WriteOnlyPacket;
import java.io.IOException;

public class ClientSettingPacket extends WriteOnlyPacket {

	public ClientSettingPacket() throws IOException {
		super(0x04);
		// locale
		writeString("en_US");
		// render distance clientside
		writeByte((byte) 0);
		// chat mode
		writeVarInt(0);
		// do we want chat colors?
		writeBoolean(true);
		// displayed skin parts, for now nothing
		writeByte((byte) 0);
		// mainhand, default to right one
		writeVarInt(1);
	}
}
