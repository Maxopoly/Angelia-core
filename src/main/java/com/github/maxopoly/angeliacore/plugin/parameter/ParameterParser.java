package com.github.maxopoly.angeliacore.plugin.parameter;

public interface ParameterParser <T>{
	
	T parse(String value);
	
	Class<T> getClassParsed();

}
