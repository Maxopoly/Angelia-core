package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.model.item.Hand;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class UseItemPacket extends WriteOnlyPacket {

	public UseItemPacket(Hand hand) throws IOException {
		super(0x1D);
		writeVarInt(hand.toInt());
	}

}
