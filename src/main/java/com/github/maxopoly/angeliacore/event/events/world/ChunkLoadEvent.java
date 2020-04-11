package com.github.maxopoly.angeliacore.event.events.world;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.block.Chunk;

/**
 * Called when a new chunk to load has been received from the server
 *
 */
public class ChunkLoadEvent implements AngeliaEvent {

	private Chunk chunk;

	public ChunkLoadEvent(Chunk chunk) {
		this.chunk = chunk;
	}

	public Chunk getChunk() {
		return chunk;
	}

}
