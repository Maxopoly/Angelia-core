package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class ByteParameterParser implements ParameterParser<Byte> {

	@Override
	public Byte parse(String value) {
		try {
			return Byte.parseByte(value);
		} catch (NumberFormatException e) {
			throw new InvalidParameterValueException();
		}
	}

	@Override
	public Class<Byte> getClassParsed() {
		return Byte.class;
	}
}
