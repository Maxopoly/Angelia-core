package com.github.maxopoly.angeliacore.model.block;

import com.github.maxopoly.angeliacore.model.block.states.BlockState;

public class Chunk {

	public static final int CHUNK_HEIGHT = 256;
	public static final int SECTION_HEIGHT = 16;
	public static final int CHUNK_WIDTH = 16;
	public static final int BLOCKS_PER_LAYER = CHUNK_WIDTH * CHUNK_WIDTH;
	public static final int BLOCKS_PER_SECTION = BLOCKS_PER_LAYER * SECTION_HEIGHT;
	public static final int SECTIONS_PER_CHUNK = CHUNK_HEIGHT / SECTION_HEIGHT;

	private final ChunkSection[] sections;
	private final int x;
	private final int z;

	public Chunk(int x, int z) {
		this.sections = new ChunkSection[SECTIONS_PER_CHUNK];
		this.x = x;
		this.z = z;
	}

	/**
	 * Dumps entire content array. Should only be used by rendering code
	 * 
	 * @return Array containing all blockstates of the chunk, separated by chunk
	 *         section
	 */
	public BlockState[][] dump() {
		BlockState[][] blocks = new BlockState[SECTIONS_PER_CHUNK][];
		for (int i = 0; i < SECTIONS_PER_CHUNK; i++) {
			if (sections[i] == null) {
				continue;
			}
			blocks[i] = sections[i].dump();
		}
		return blocks;
	}

	public BlockState getBlock(int x, int y, int z) {
		if (y > 255 || y < 0) {
			return null;
		}
		ChunkSection section = sections[y / SECTION_HEIGHT];
		if (section == null) {
			return null;
		}
		return section.getBlock(x, y % SECTION_HEIGHT, z);
	}

	public void setBlock(int x, int y, int z, BlockState st) {
		sections[y / SECTION_HEIGHT].setBlock(x, y % SECTION_HEIGHT, z, st);
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}

	public void setSection(ChunkSection section, int index) {
		if (index >= SECTIONS_PER_CHUNK || index < 0) {
			throw new IllegalArgumentException("Invalid chunk section coordinate: " + index);
		}
		sections[index] = section;
	}

}
