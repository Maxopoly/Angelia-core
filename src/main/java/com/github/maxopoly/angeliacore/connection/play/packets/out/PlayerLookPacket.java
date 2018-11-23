package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class PlayerLookPacket extends WriteOnlyPacket {

	public PlayerLookPacket(float yaw, float pitch) throws IOException {
		super(0x0F);
		writeFloat(yaw);
		writeFloat(pitch);
		writeBoolean(true);
	}

}
