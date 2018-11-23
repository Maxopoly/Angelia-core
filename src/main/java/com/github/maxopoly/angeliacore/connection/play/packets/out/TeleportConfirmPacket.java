package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class TeleportConfirmPacket extends WriteOnlyPacket {

	public TeleportConfirmPacket(int teleportID) throws IOException {
		super(0x00);
		writeVarInt(teleportID);
	}

}
