package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class CloseWindowPacket extends WriteOnlyPacket {

	public CloseWindowPacket(byte windowID) throws IOException {
		super(0x08);
		writeByte(windowID);
	}
}
