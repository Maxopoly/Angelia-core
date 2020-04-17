package com.github.maxopoly.angeliacore.model.block;

import com.github.maxopoly.angeliacore.model.block.states.BlockState;

public class ChunkSection {

	private BlockState[] blocks;

	public ChunkSection(BlockState[] blocks) {
		this.blocks = blocks;
	}
	
	public ChunkSection() {
		this.blocks = new BlockState[16*256];
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

	public void setBlock(int x, int y, int z, BlockState st) {
		blocks[x + 16 * z + 256 * y] = st;
	}
}
