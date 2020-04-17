package com.github.maxopoly.angeliacore.model.block.states;

import com.github.maxopoly.angeliacore.model.location.BlockFace;

/**
 * Dropper or Dispenser
 *
 */
public class ActivatableFacingBlock extends FullSingleConstBlockState {

	public ActivatableFacingBlock(int id, byte metaData, float hardness, String texturePackIdentifier,
			String niceName, boolean hasCollision) {
		super(id, metaData, hardness, texturePackIdentifier, niceName, hasCollision);
	}

	/**
	 * @return In which direction is the block facing
	 */
	public BlockFace getFacingDirection() {
		int firstPart = this.metaData & 0x8;
		if (firstPart > 5) {
			throw new IllegalStateException("Facing direction is " + (byte) firstPart + " with meta data " + metaData);
		}
		return BlockFace.values()[firstPart];
	}

	@Override
	public boolean isFullBlock() {
		return true;
	}

	@Override
	public boolean isLiquid() {
		return false;
	}

	@Override
	public boolean isOpaque() {
		return true;
	}

}
