package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

/**
 * Needs to be sent on every tick so the server keeps properly updating the player location
 *
 */
public class PlayerStatePacket extends WriteOnlyPacket {

	public PlayerStatePacket(boolean onGround) throws IOException {
		super(0x0F);
		// this bool specifies whether the client is on the ground or not
		writeBoolean(onGround);
	}

}
