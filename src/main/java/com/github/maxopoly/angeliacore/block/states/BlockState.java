package com.github.maxopoly.angeliacore.block.states;

import java.util.List;

import com.github.maxopoly.angeliacore.block.RenderModule;

public abstract class BlockState {

    protected int id;
    protected byte metaData;
    protected float hardness;
    protected String texturePackIdentifier;
    protected String niceName;
    protected RenderModule render;

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
    
    public void setRenderModule(RenderModule renderModule) {
    	this.render = renderModule;
    }
    
    public RenderModule getRenderModule() {
    	return render;
    }

    public abstract boolean isFullBlock();

    public abstract boolean isLiquid();

    public abstract boolean isOpaque();
    
    public abstract BlockState getActualState(byte data);
    
    public abstract int getMetaData(List<Enum> enums);

    @Override
    public String toString() {
        return niceName != null ? niceName : id + ":" + metaData;
    }

}
