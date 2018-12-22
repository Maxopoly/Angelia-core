package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.libs.packetEncoding.WriteOnlyPacket;
import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.model.location.Location;

public class PlayerDiggingPacket extends WriteOnlyPacket {

	public PlayerDiggingPacket(int status, Location loc, BlockFace face) throws IOException {
		super(0x14);
		writeVarInt(status);
		writePosition((int) loc.getX(), (int) loc.getY(), (int) loc.getZ());
		writeByte(face.getEnumByte());
	}
}
