package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;
import com.github.maxopoly.angeliacore.plugin.parameter.ParameterParser;

public class BooleanParameterParser implements ParameterParser<Boolean>{

	@Override
	public Boolean parse(String value) {
		switch(value.toLowerCase()) {
		case "1":
		case "true":
		case "t":
			return true;
		case "0":
		case "false":
		case "f":
			return false;
		case "null":
			return null;
		}
 		throw new InvalidParameterValueException();
	}

	@Override
	public Class<Boolean> getClassParsed() {
		return Boolean.class;
	}
}
