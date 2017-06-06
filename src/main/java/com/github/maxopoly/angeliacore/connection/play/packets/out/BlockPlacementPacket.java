package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.model.Location;
import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class BlockPlacementPacket extends WriteOnlyPacket {

	public BlockPlacementPacket(Location blockLoc, int face) throws IOException {
		super(0x1C);
		writePosition((int) blockLoc.getX(), (int) blockLoc.getY() - 1, (int) blockLoc.getZ());
		writeVarInt(face);
		writeVarInt(0);
		writeVarInt(7);
		writeVarInt(7);
		writeVarInt(7);
	}

}
