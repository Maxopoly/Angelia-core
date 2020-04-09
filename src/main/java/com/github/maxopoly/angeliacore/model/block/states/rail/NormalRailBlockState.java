package com.github.maxopoly.angeliacore.model.block.states.rail;

public class NormalRailBlockState extends GeneralRailBlockState {

	public NormalRailBlockState(int id, byte metaData, float hardness, String texturePackIdentifier, String niceName) {
		super(id, metaData, hardness, texturePackIdentifier, niceName);
		if (metaData > 9) {
			throw new IllegalStateException("Rail can not have meta data " + metaData);
		}
	}

	public RailStateEnum getRailState() {
		return RailStateEnum.values()[metaData];
	}
}
