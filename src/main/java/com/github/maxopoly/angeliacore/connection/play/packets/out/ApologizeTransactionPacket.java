package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;

/**
 * If an item transaction failed, the client has to apologize with this packet,
 * acknowledging that the transaction failed, otherwise further transactions
 * will all be blocked
 *
 */
public class ApologizeTransactionPacket extends WriteOnlyPacket {

	public ApologizeTransactionPacket(byte windowID, short actionID) throws IOException {
		super(0x05);
		writeByte(windowID);
		writeShort(actionID);
		// this packet is only sent if a transaction is failed, so we might as well
		// always write false here
		writeBoolean(false);
	}

}
