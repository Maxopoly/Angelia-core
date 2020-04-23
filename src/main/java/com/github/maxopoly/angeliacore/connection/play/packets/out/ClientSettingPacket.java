package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class ClientSettingPacket extends WriteOnlyPacket {

	public ClientSettingPacket() throws IOException {
		super(0x04);
		// locale
		writeString("en_US");
		// render distance clientside
		writeByte((byte) 3);
		// chat mode
		writeVarInt(0);
		// do we want chat colors?
		writeBoolean(false);
		// displayed skin parts, for now nothing
		writeByte((byte) 0);
		// mainhand, default to right one
		writeVarInt(1);
	}
}
