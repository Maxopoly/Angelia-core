package com.github.maxopoly.angeliacore.block.states;

public abstract class BlockState {


	private int id;
	private byte metaData;

	public BlockState(int id, byte metaData) {
		this.id = id;
		this.metaData = metaData;
	}

	public int getID() {
		return id;
	}

	public byte getMetaData() {
		return metaData;
	}

}
