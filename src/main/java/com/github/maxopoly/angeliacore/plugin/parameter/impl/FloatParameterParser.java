package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class FloatParameterParser implements ParameterParser<Float> {

	@Override
	public Class<Float> getClassParsed() {
		return Float.class;
	}

	@Override
	public Float parse(String value) {
		try {
			return Float.parseFloat(value);
		} catch (NumberFormatException e) {
			throw new InvalidParameterValueException();
		}
	}
}
