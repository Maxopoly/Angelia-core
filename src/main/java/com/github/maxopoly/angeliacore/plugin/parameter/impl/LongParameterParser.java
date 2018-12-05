package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class LongParameterParser implements ParameterParser<Long> {

	@Override
	public Long parse(String value) {
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new InvalidParameterValueException();
		}
	}

	@Override
	public Class<Long> getClassParsed() {
		return Long.class;
	}
}
