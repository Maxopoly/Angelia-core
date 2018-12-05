package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class IntegerParameterParser implements ParameterParser<Integer> {

	@Override
	public Integer parse(String value) {
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new InvalidParameterValueException();
		}
	}

	@Override
	public Class<Integer> getClassParsed() {
		return Integer.class;
	}
}
