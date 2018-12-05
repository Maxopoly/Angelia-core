package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.model.location.MovementDirection;
import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;

public class MovementDirectionParameterParser extends AbstractObjectParameterParser<MovementDirection> {

	@Override
	public MovementDirection parseObject(String value) {
		try {
			return MovementDirection.valueOf(value.toUpperCase());
		}
		catch (IllegalArgumentException e) {
			throw new InvalidParameterValueException();
		}
	}

	@Override
	public Class<MovementDirection> getClassParsed() {
		return MovementDirection.class;
	}
}
