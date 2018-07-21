package com.github.maxopoly.angeliacore.block;

import com.github.maxopoly.angeliacore.block.states.BlockState;

public class ChunkSection {

	private BlockState [] blocks;


	public ChunkSection(BlockState [] blocks) {
		this.blocks = blocks;
	}

	public BlockState getBlock(int x, int y, int z) {
		return blocks [x + 16 * z + 256 * y ];
	}

	/**
	 * Dumps entire content array. Should only be used by rendering code
	 */
	public BlockState [] dump() {
	    return blocks;
	}
}
