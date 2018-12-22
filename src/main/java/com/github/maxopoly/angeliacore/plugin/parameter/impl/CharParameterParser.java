package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class CharParameterParser implements ParameterParser<Character> {

	@Override
	public Class<Character> getClassParsed() {
		return Character.class;
	}

	@Override
	public Character parse(String value) {
		if (value.length() != 1) {
			throw new InvalidParameterValueException();
		}
		return value.charAt(0);
	}
}
