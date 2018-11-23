package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.model.block.Chunk;

/**
 * Called when a chunk is unloaded from memory
 *
 */
public class ChunkUnloadEvent implements AngeliaEvent {

    private Chunk chunk;

    public ChunkUnloadEvent(Chunk chunk) {
        this.chunk = chunk;
    }

    public Chunk getChunk() {
        return chunk;
    }

}
