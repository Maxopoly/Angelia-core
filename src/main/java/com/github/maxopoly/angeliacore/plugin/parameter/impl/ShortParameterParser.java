package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class ShortParameterParser implements ParameterParser<Short> {

	@Override
	public Short parse(String value) {
		try {
			return Short.parseShort(value);
		} catch (NumberFormatException e) {
			throw new InvalidParameterValueException();
		}
	}

	@Override
	public Class<Short> getClassParsed() {
		return Short.class;
	}
}
