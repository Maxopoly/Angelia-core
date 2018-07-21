package com.github.maxopoly.angeliacore.block.states;

public abstract class BlockState {

    private int id;
    private byte metaData;
    private float hardness;
    private String texturePackIdentifier;
    private String niceName;

    public BlockState(int id, byte metaData, float hardness, String texturePackIdentifier, String niceName) {
        this.id = id;
        this.metaData = metaData;
        this.hardness = hardness;
        this.texturePackIdentifier = texturePackIdentifier;
        this.niceName = niceName;
    }

    public int getID() {
        return id;
    }

    public byte getMetaData() {
        return metaData;
    }

    public double getHardness() {
        return hardness;
    }

    public String getTexturePackIdentifier() {
        return texturePackIdentifier;
    }

    public abstract boolean isFullBlock();

    public abstract boolean isLiquid();

    public abstract boolean isOpaque();

    @Override
    public String toString() {
        return niceName != null ? niceName : id + ":" + metaData;
    }

}
