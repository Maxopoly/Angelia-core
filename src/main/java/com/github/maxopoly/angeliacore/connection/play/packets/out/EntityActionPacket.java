package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.packet.WriteOnlyPacket;

public class EntityActionPacket extends WriteOnlyPacket {

	public EntityActionPacket(int entityID, int actionID, int jumpBoost) throws IOException {
		super(0x15);
		writeVarInt(entityID);
		writeVarInt(actionID);
		writeVarInt(jumpBoost);
	}

}

