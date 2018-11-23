package com.github.maxopoly.angeliacore.model.block;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.github.maxopoly.angeliacore.config.GlobalConfig;

/**
 * Holds all chunks, which contain all block data. For performance reasons
 * holding block data can be disabled via the global config. If any methods are
 * called while no block data is held, a BlockModelNotHeldException will be thrown
 *
 */
public class ChunkHolder {

	private Map<Long, Chunk> loadedChunks;
	private boolean active;

	public ChunkHolder(GlobalConfig config) {
		active = config.holdBlockModel();
		if (active) {
			loadedChunks = new TreeMap<Long, Chunk>();
		}
	}

	/**
	 * Do not call this directly, set the value in the config instead
	 */
	public void setActivationState(boolean state) {
		if (state == active) {
			return;
		}
		this.active = state;
		if (state) {
			// activate
			loadedChunks = new TreeMap<Long, Chunk>();
		} else {
			// deactivate, GC will handle this for us
			loadedChunks = null;
			System.gc();
		}
	}

	/**
	 * @return Whether a representation of block data is being held or not
	 */
	public boolean isHoldingModel() {
		return active;
	}

	public Chunk getChunk(int x, int z) {
		ensureActive();
		return loadedChunks.get(encodeCoords(x, z));
	}

	public void removeChunk(int x, int z) {
		ensureActive();
		loadedChunks.remove(encodeCoords(x, z));
	}

	public void putChunk(Chunk chunk) {
		ensureActive();
		loadedChunks.put(encodeCoords(chunk.getX(), chunk.getZ()), chunk);
	}

	private long encodeCoords(int x, int z) {
		return (((long) x) << 32) | z;
	}

	public Collection<Chunk> getLoadedChunks() {
		ensureActive();
		return loadedChunks.values();
	}

	private void ensureActive() {
		if (!active) {
			throw new BlockModelNotHeldException(
					"Block model holding is disabled, change block.holdModel to enable it");
		}
	}

}
