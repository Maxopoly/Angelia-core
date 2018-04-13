package com.github.maxopoly.angeliacore.block;

public class Chunk {

	public static final int CHUNK_HEIGHT = 256;
	public static final int SECTION_HEIGHT = 16;
	public static final int SECTION_WIDTH = 16;
	public static final int SECTIONS_PER_CHUNK = CHUNK_HEIGHT / SECTION_HEIGHT;

	private final ChunkSection [] sections;
	private final int x;
	private final int z;

	public Chunk(int x, int z) {
		this.sections = new ChunkSection [16];
		this.x = x;
		this.z = z;
	}

	public void setSection(ChunkSection section, int index) {
		if (index >= SECTIONS_PER_CHUNK || index < 0) {
			throw new IllegalArgumentException("Invalid chunk section coordinate: " + index);
		}
		sections [index] = section;
	}

	public int getX() {
		return x;
	}

	public int getZ() {
		return z;
	}



}
