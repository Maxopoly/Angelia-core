package com.github.maxopoly.angeliacore.model.block;

import com.github.maxopoly.angeliacore.model.block.states.BlockState;

public class ChunkSection {

	private BlockState[] blocks;

	public ChunkSection(BlockState[] blocks) {
		this.blocks = blocks;
	}

	/**
	 * Dumps entire content array. Should only be used by rendering code
	 * 
	 * @return Internal array of block data
	 */
	public BlockState[] dump() {
		return blocks;
	}

	public BlockState getBlock(int x, int y, int z) {
		return blocks[x + 16 * z + 256 * y];
	}
}
