package com.github.maxopoly.angeliacore.model.block.states;

/**
 * Stuff like saplings with custom models
 *
 */
public class FancySpecialBlockState extends FullSingleConstBlockState {

	public FancySpecialBlockState(int id, byte metaData, float hardness, String texturePackIdentifier,
			String niceName) {
		super(id, metaData, hardness, texturePackIdentifier, niceName, false);
	}

	public FancySpecialBlockState(int id, float hardness, String texturePackIdentifier, String niceName) {
		this(id, (byte) 0, hardness, texturePackIdentifier, niceName);
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
