package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class HeldItemChangePacket extends WriteOnlyPacket {

	public HeldItemChangePacket(int slot) throws IOException {
		super(0x1A);
		writeShort((short) slot);
	}

}
