package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.model.location.BlockFace;
import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;

public class BlockFaceParameterParser extends AbstractObjectParameterParser<BlockFace> {

	@Override
	public BlockFace parseObject(String value) {
		try {
			return BlockFace.parse(value);
		}
		catch (IllegalArgumentException e) {
			throw new InvalidParameterValueException();
		}
	}

	@Override
	public Class<BlockFace> getClassParsed() {
		return BlockFace.class;
	}
}
