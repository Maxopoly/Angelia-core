package com.github.maxopoly.angeliacore.event.events.entity;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.location.Location;

public class EntityBreakingBlockEvent implements AngeliaEvent {
	int entityID;
	Location blockPos;
	byte stage;

	public EntityBreakingBlockEvent(int entityID, Location blockPos, byte stage) {
		super();
		this.entityID = entityID;
		this.blockPos = blockPos;
		this.stage = stage;
	}

	public int getEntityID() {
		return entityID;
	}

	public Location getBlockPos() {
		return blockPos;
	}

	public byte getStage() {
		return stage;
	}

	public BlockState getBlock(ServerConnection conn) {
		return this.blockPos.getBlockAt(conn);
	}

}
