package com.github.maxopoly.angeliacore.block.states;

public class AirBlockState extends SingleConstBlockState {

	public AirBlockState() {
		super(0, (byte) 0, 0f, "air", "Air");
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
