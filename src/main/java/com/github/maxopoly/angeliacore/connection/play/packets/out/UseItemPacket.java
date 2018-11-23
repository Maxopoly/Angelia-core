package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;
import com.github.maxopoly.angeliacore.model.item.Hand;
import java.io.IOException;

public class UseItemPacket extends WriteOnlyPacket {

	public UseItemPacket(Hand hand) throws IOException {
		super(0x20);
		writeVarInt(hand.toInt());
	}

}
