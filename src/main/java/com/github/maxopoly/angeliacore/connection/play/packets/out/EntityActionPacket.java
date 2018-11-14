package com.github.maxopoly.angeliacore.connection.play.packets.out;

import java.io.IOException;

import com.github.maxopoly.angeliacore.binary.WriteOnlyPacket;

public class EntityActionPacket extends WriteOnlyPacket {
	
	public EntityActionPacket(int entityID, Action type) throws IOException {
		this(entityID, type, 0);
	}

	public EntityActionPacket(int entityID, Action type, int horseJumpStrength) throws IOException {
		super(0x15);
		writeVarInt(entityID);
		writeVarInt(type.index);
		writeVarInt(horseJumpStrength);
	}

	public enum Action {
		START_SNEAKING(0), STOP_SNEAKING(1), LEAVE_BED(2), START_SPRINTING(3), STOP_SPRINTING(4), START_HORSE_JUMP(5),
		STOP_HORSE_JUMP(6), OPEN_HORSE_INVENTORY(7), START_ELYRA_FLIGHT(8);
		
		private int index;
		
		private Action(int index) {
			this.index = index;
		}
	}

}
