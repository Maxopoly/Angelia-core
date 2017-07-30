package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class ClickWindowPacket extends WriteOnlyPacket {

	public ClickWindowPacket(byte windowID, short slot, byte button, short actionID, int mode, ItemStack clicked)
			throws IOException {
		super(0x07);
		writeByte(windowID);
		writeShort(slot);
		writeByte(button);
		writeShort(actionID);
		writeVarInt(mode);
		writeItemStack(clicked);
	}

}
