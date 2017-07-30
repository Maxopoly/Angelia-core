package com.github.maxopoly.angeliacore.connection.play.packets.out;

import com.github.maxopoly.angeliacore.model.location.BlockFace;

import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;
import java.io.IOException;

public class BlockPlacementPacket extends WriteOnlyPacket {

	public BlockPlacementPacket(Location blockLoc, BlockFace face, int cursorX, int cursorY, int cursorZ)
			throws IOException {
		super(0x1C);
		writePosition((int) blockLoc.getX(), (int) blockLoc.getY(), (int) blockLoc.getZ());
		writeByte(face.getEnumByte());
		writeVarInt(0);
		writeVarInt(cursorX);
		writeVarInt(cursorY);
		writeVarInt(cursorZ);
	}

	public BlockPlacementPacket(Location blockLoc, BlockFace face) throws IOException {
		this(blockLoc, face, face.placementVectorX(), face.placementVectorY(), face.placementVectorZ());
	}

}
