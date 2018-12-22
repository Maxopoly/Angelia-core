package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

/**
 * Needs to be sent on every tick so the server keeps properly updating the
 * player location
 *
 */
public class PlayerStatePacket extends WriteOnlyPacket {

	public PlayerStatePacket(boolean onGround) throws IOException {
		super(0x0C);
		// this bool specifies whether the client is on the ground or not
		writeBoolean(onGround);
	}

}
