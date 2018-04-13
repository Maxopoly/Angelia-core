package com.github.maxopoly.angeliacore.block;

import com.github.maxopoly.angeliacore.block.states.BlockState;

public class ChunkSection {

	private BlockState [] blocks;


	public ChunkSection(BlockState [] blocks) {
		this.blocks = blocks;
	}

	public BlockState getBlock(int x, int y, int z) {
		if (x > 15 || x < 0 || y > 15 || y < 0 ||z > 15 || z < 0) {
			throw new IllegalArgumentException(String.format("getBlock out of bounds %d %d %d", x,y,z));
		}
		return blocks [x + 16 * z + 256 * y ];
	}
}
