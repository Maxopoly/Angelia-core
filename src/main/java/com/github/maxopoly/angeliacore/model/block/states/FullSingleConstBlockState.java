package com.github.maxopoly.angeliacore.model.block.states;

import java.util.List;

public abstract class FullSingleConstBlockState extends BlockState {

	public FullSingleConstBlockState(int id, byte metaData, float hardness, String texturePackIdentifier, String niceName, boolean hasCollision) {
		super(id, metaData, hardness, texturePackIdentifier, niceName, hasCollision);
	}

	@Override
	public BlockState getActualState(byte data) {
		return this;
	}

	@Override
	public int getMetaData(@SuppressWarnings("rawtypes") List<Enum> enums) {
		return 0;
	}

}
