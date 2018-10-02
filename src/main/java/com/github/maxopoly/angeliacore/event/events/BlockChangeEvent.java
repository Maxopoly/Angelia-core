package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.block.Block;

public class BlockChangeEvent implements AngeliaEvent {

    private Block changedFrom;
    private Block changedTo;

    public BlockChangeEvent(Block from, Block to) {
        this.changedFrom = from;
        this.changedTo = to;
    }

    public Block getPreviousBlock() {
        return changedFrom;
    }

    public Block getBlock() {
        return changedTo;
    }

}
