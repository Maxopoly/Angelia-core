package com.github.maxopoly.angeliacore.model.block.states;

public class AirBlockState extends FullSingleConstBlockState {

	public AirBlockState() {
		super(0, (byte) 0, 0f, "air", "Air", false);
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
