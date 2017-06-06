package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class BreakAnimationPacket extends WriteOnlyPacket {

	public BreakAnimationPacket() throws IOException {
		super(0x1A);
		writeVarInt(0);
	}
}
