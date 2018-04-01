package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class PlayerPositionPacket extends WriteOnlyPacket {

	public PlayerPositionPacket(double x, double y, double z, boolean onGround) throws IOException {
		super(0x0D);
		writeDouble(x);
		writeDouble(y);
		writeDouble(z);
		writeBoolean(onGround);
	}

}
