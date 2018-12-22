package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class StringParameterParser implements ParameterParser<String> {

	@Override
	public Class<String> getClassParsed() {
		return String.class;
	}

	@Override
	public String parse(String value) {
		return value;
	}
}