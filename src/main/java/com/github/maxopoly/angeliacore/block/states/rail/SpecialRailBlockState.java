package com.github.maxopoly.angeliacore.block.states.rail;

import com.github.maxopoly.angeliacore.block.states.PowerableStateEnum;

public class SpecialRailBlockState extends GeneralRailBlockState {

	public SpecialRailBlockState(int id, byte metaData, float hardness, String texturePackIdentifier, String niceName) {
		super(id, metaData, hardness, texturePackIdentifier, niceName);
		if ((metaData & 0x8) >= SpecialRailStateEnum.values().length) {
			throw new IllegalArgumentException("Can not instanciate special rail with meta " + metaData);
		}
	}
	
	public SpecialRailStateEnum getRailState() {
		return SpecialRailStateEnum.values() [metaData & 0x8];
	}
	
	public PowerableStateEnum getPowerState() {
		return metaData > 7 ? PowerableStateEnum.POWERED: PowerableStateEnum.NOT_POWERED;
	}
}
