package com.github.maxopoly.angeliacore.block;

import com.github.maxopoly.angeliacore.model.location.Location;

public class Block {;

    private BlockState state;
    private Location location;

    public Block(BlockState state, Location location) {
        this.state = state;
        this.location = location;
    }

    public BlockState getBlockType() {
        return state;
    }

    public int getX() {
        return location.getBlockX();
    }

    public int getY() {
        return location.getBlockY();
    }

    public int getZ() {
        return location.getBlockZ();
    }

    @Override
    public String toString() {
        return state.name() + "[" + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ() + "]";
    }

}
