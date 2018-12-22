package com.github.maxopoly.angeliacore.plugin.parameter;

public interface ParameterParser<T> {

	Class<T> getClassParsed();

	T parse(String value);

}
