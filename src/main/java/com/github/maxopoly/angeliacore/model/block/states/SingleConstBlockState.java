package com.github.maxopoly.angeliacore.model.block.states;

import java.util.List;

public abstract class SingleConstBlockState extends BlockState {

	public SingleConstBlockState(int id, byte metaData, float hardness, String texturePackIdentifier, String niceName) {
		super(id, metaData, hardness, texturePackIdentifier, niceName);
	}

	@Override
	public BlockState getActualState(byte data) {
		return this;
	}

	@Override
	public int getMetaData(List<Enum> enums) {
		return 0;
	}

}
