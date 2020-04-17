package com.github.maxopoly.angeliacore.model.block.states.rail;

import com.github.maxopoly.angeliacore.model.block.states.FullSingleConstBlockState;

public abstract class GeneralRailBlockState extends FullSingleConstBlockState {

	public GeneralRailBlockState(int id, byte metaData, float hardness, String texturePackIdentifier, String niceName) {
		super(id, metaData, hardness, texturePackIdentifier, niceName, false);
	}

	@Override
	public boolean isFullBlock() {
		return false;
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

	@Override
	public boolean isOpaque() {
		return false;
	}

}
