package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.model.Location;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class PlayerDiggingPacket extends WriteOnlyPacket {

	public PlayerDiggingPacket(int status, Location loc, byte face) throws IOException {
		super(0x13);
		writeVarInt(status);
		writePosition((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
		writeByte(face);
	}
}
