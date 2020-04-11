package com.github.maxopoly.angeliacore.model.block.states;

import java.util.List;

import com.github.maxopoly.angeliacore.model.block.RenderModule;
import com.github.maxopoly.angeliacore.model.entity.AABB;

public abstract class BlockState {

	protected int id;
	protected byte metaData;
	protected float hardness;
	protected String texturePackIdentifier;
	protected String niceName;
	protected RenderModule render;
	protected AABB boundingBox;

	public BlockState(int id, byte metaData, float hardness, String texturePackIdentifier, String niceName) {
		this.id = id;
		this.metaData = metaData;
		this.hardness = hardness;
		this.texturePackIdentifier = texturePackIdentifier;
		this.niceName = niceName;
		this.boundingBox = new AABB(0,1,0,1,0,1);
	}

	public abstract BlockState getActualState(byte data);

	public double getHardness() {
		return hardness;
	}

	public int getID() {
		return id;
	}

	public byte getMetaData() {
		return metaData;
	}

	public abstract int getMetaData(@SuppressWarnings("rawtypes") List<Enum> enums);

	public RenderModule getRenderModule() {
		return render;
	}

	public String getTexturePackIdentifier() {
		return texturePackIdentifier;
	}

	public abstract boolean isFullBlock();

	public abstract boolean isLiquid();

	public abstract boolean isOpaque();

	public void setRenderModule(RenderModule renderModule) {
		this.render = renderModule;
	}

	public AABB getBoundingBox() {
		return boundingBox;
	}

	@Override
	public String toString() {
		return niceName != null ? niceName : (id + ":" + metaData);
	}

}
