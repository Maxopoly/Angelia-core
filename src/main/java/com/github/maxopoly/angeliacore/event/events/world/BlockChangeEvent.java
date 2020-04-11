package com.github.maxopoly.angeliacore.event.events.world;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;

public class BlockChangeEvent implements AngeliaEvent {
	private BlockState oldBlock;
	private BlockState newBlock;

	public BlockChangeEvent(BlockState oldBlock, BlockState newBlock) {
		super();
		this.oldBlock = oldBlock;
		this.newBlock = newBlock;
	}

	public BlockState getOldBlock() {
		return oldBlock;
	}

	public BlockState getNewBlock() {
		return newBlock;
	}

}
