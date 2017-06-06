package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class MovePacket extends WriteOnlyPacket {

	public MovePacket(double x, double y, double z) throws IOException {
		super(0x0C);
		writeDouble(x);
		writeDouble(y);
		writeDouble(z);
		writeBoolean(true);
	}

}
