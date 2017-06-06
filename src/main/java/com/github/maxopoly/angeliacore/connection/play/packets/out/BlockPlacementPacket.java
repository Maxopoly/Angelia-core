package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.model.ItemStack;
import com.github.maxopoly.angeliacore.model.Location;
import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class BlockPlacementPacket extends WriteOnlyPacket {

	public BlockPlacementPacket(Location blockLoc, ItemStack is, int face) throws IOException {
		super(0x1C);
		writePosition((int) blockLoc.getX(), (int) blockLoc.getY(), (int) blockLoc.getZ());
		writeByte((byte) face);
		writeItemStack(is);
		writeFloat(0.5f);
		writeFloat(0.5f);
		writeFloat(0.5f);
	}

}
