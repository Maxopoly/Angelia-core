package com.github.maxopoly.angeliacore.block;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

public class ChunkHolder {

	private Map<Long, Chunk> loadedChunks;

	public ChunkHolder() {
		loadedChunks = new TreeMap<Long, Chunk>();
	}

	public Chunk getChunk(int x, int z) {
		return loadedChunks.get(encodeCoords(x, z));
	}

	public void putChunk(Chunk chunk) {
		loadedChunks.put(encodeCoords(chunk.getX(), chunk.getZ()), chunk);
	}

	private long encodeCoords(int x, int z) {
		return (((long) x) << 32) | z;
	}

	public Collection <Chunk> getLoadedChunks() {
		return loadedChunks.values();
	}



}
