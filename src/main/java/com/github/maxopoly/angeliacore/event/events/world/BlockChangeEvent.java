package com.github.maxopoly.angeliacore.event.events.world;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.location.Location;

public class BlockChangeEvent implements AngeliaEvent {
	
	private BlockState oldBlock;
	private BlockState newBlock;
	private Location location;

	public BlockChangeEvent(Location location, BlockState oldBlock, BlockState newBlock) {
		this.location = location;
		this.oldBlock = oldBlock;
		this.newBlock = newBlock;
	}
	
	public Location getLocation() {
		return location;
	}

	public BlockState getOldBlock() {
		return oldBlock;
	}

	public BlockState getNewBlock() {
		return newBlock;
	}

}
