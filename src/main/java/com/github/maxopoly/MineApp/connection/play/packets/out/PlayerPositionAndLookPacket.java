package com.github.maxopoly.MineApp.connection.play.packets.out;

import com.github.maxopoly.MineApp.packet.WriteOnlyPacket;
import java.io.IOException;

public class PlayerPositionAndLookPacket extends WriteOnlyPacket {

	public PlayerPositionAndLookPacket(double x, double y, double z, float yaw, float pitch, boolean onGround)
			throws IOException {
		super(0x0D);
		writeDouble(x);
		writeDouble(y);
		writeDouble(z);
		writeFloat(yaw);
		writeFloat(pitch);
		// this bool specifies whether the client is on the ground or not
		writeBoolean(onGround);
	}

}
