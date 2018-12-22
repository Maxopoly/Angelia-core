package com.github.maxopoly.angeliacore.block.states.rail;

import com.github.maxopoly.angeliacore.block.states.SingleConstBlockState;

public abstract class GeneralRailBlockState extends SingleConstBlockState {

	public GeneralRailBlockState(int id, byte metaData, float hardness, String texturePackIdentifier, String niceName) {
		super(id, metaData, hardness, texturePackIdentifier, niceName);
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
