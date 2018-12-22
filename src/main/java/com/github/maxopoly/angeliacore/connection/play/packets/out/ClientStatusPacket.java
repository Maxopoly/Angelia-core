package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

public class ClientStatusPacket extends WriteOnlyPacket {

	/**
	 * Sent when the client is ready to complete login, when the client is ready to
	 * respawn after death or to request specific info
	 * 
	 * @param action 0 = Perform respawn, 1 = Request stats, 2 = open inventory
	 * @throws IOException Should never happen (in theory)
	 */
	public ClientStatusPacket(int action) throws IOException {
		super(0x03);
		writeVarInt(action);
	}

}
