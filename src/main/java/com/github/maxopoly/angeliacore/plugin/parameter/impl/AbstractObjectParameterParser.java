package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public abstract class AbstractObjectParameterParser <T> implements ParameterParser<T> {

	@Override
	public T parse(String value) {
		String cleaned = value.trim().toLowerCase();
		if(cleaned.equals("null")) {
			return null;
		}
		return parseObject(cleaned);
	}
	
	public abstract T parseObject(String value);
}
