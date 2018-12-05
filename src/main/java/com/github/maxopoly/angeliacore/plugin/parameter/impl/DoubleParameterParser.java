package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class DoubleParameterParser implements ParameterParser<Double> {

	@Override
	public Double parse(String value) {
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException e) {
			throw new InvalidParameterValueException();
		}
	}

	@Override
	public Class<Double> getClassParsed() {
		return Double.class;
	}
}
