package com.github.maxopoly.angeliacore.model.block;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import com.github.maxopoly.angeliacore.libs.yaml.config.GlobalConfig;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.location.Location;

/**
 * Holds all chunks, which contain all block data. For performance reasons
 * holding block data can be disabled via the global config. If any methods are
 * called while no block data is held, a BlockModelNotHeldException will be
 * thrown
 *
 */
public class ChunkHolder {

	private Map<Long, Chunk> loadedChunks;
	private boolean active;

	public ChunkHolder(GlobalConfig config) {
		active = config.holdBlockModel();
		if (active) {
			loadedChunks = new TreeMap<>();
		}
	}
	
	public BlockState getBlockAt(Location location) {
		Chunk chunk = getChunk(convertToChunkCoord(location.getBlockX()), convertToChunkCoord(location.getBlockZ()));
		if (chunk == null) {
			//TODO exception?
			return null;
		}
		return chunk.getBlock(location.getBlockX(), location.getBlockY(), location.getBlockZ());
	}
	
	private static int convertToChunkCoord(int rawCoord) {
		int div = rawCoord / 16;
		if (rawCoord < 0) {
			return div - 1;
		}
		return div;
	}

	private static long encodeCoords(int x, int z) {
		return (((long) x) << 32) | (z & 0xFF_FF_FF_FF);
	}

	private void ensureActive() {
		if (!active) {
			throw new BlockModelNotHeldException(
					"Block model holding is disabled, change block.holdModel to enable it");
		}
	}

	public Chunk getChunk(int x, int z) {
		ensureActive();
		return loadedChunks.get(encodeCoords(x, z));
	}

	public Collection<Chunk> getLoadedChunks() {
		ensureActive();
		return loadedChunks.values();
	}

	/**
	 * @return Whether a representation of block data is being held or not
	 */
	public boolean isHoldingModel() {
		return active;
	}

	public void putChunk(Chunk chunk) {
		ensureActive();
		loadedChunks.put(encodeCoords(chunk.getX(), chunk.getZ()), chunk);
	}

	public void removeChunk(int x, int z) {
		ensureActive();
		loadedChunks.remove(encodeCoords(x, z));
	}

	/**
	 * Do not call this directly, set the value in the config instead
	 * 
	 * @param state State to switch to
	 */
	public void setActivationState(boolean state) {
		if (state == active) {
			return;
		}
		this.active = state;
		if (state) {
			// activate
			loadedChunks = new TreeMap<>();
		} else {
			// deactivate, GC will handle this for us
			loadedChunks = null;
			System.gc();
		}
	}

}
