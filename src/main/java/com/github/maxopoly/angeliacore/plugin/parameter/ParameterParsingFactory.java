package com.github.maxopoly.angeliacore.plugin.parameter;

import java.util.HashMap;
import java.util.Map;

import com.github.maxopoly.angeliacore.plugin.parameter.impl.BlockFaceParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.BooleanParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.ByteParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.CharParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.DoubleParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.FloatParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.IntegerParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.LocationParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.LongParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.MovementDirectionParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.ShortParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.StringParameterParser;
import com.github.maxopoly.angeliacore.plugin.parameter.impl.VectorParameterParser;

public class ParameterParsingFactory {

	private Map<Class<?>, ParameterParser<?>> parserMap;
	private Map<Class<?>, Class<?>> wrapperClasses;

	public ParameterParsingFactory() {
		loadParser();
	}

	@SuppressWarnings("unchecked")
	private <T> Class<T> convertPrimitive(Class<T> primType) {
		return (Class<T>) wrapperClasses.get(primType);
	}

	@SuppressWarnings("unchecked")
	public <T> ParameterParser<T> getParser(Class<T> classType) {
		if (classType.isPrimitive()) {
			classType = convertPrimitive(classType);
		}
		ParameterParser<?> parser = parserMap.get(classType);
		if (parser == null) {
			// need to do this explicitly, otherwise cast will throw exception
			return null;
		}
		return (ParameterParser<T>) parser;
	}

	private void loadParser() {
		parserMap = new HashMap<Class<?>, ParameterParser<?>>();
		readyParser(new BlockFaceParameterParser());
		readyParser(new BooleanParameterParser());
		readyParser(new ByteParameterParser());
		readyParser(new CharParameterParser());
		readyParser(new DoubleParameterParser());
		readyParser(new FloatParameterParser());
		readyParser(new IntegerParameterParser());
		readyParser(new LocationParameterParser());
		readyParser(new LongParameterParser());
		readyParser(new MovementDirectionParameterParser());
		readyParser(new ShortParameterParser());
		readyParser(new VectorParameterParser());
		readyParser(new StringParameterParser());
		wrapperClasses = new HashMap<Class<?>, Class<?>>();
		wrapperClasses.put(boolean.class, Boolean.class);
		wrapperClasses.put(byte.class, Byte.class);
		wrapperClasses.put(char.class, Character.class);
		wrapperClasses.put(double.class, Double.class);
		wrapperClasses.put(float.class, Float.class);
		wrapperClasses.put(int.class, Integer.class);
		wrapperClasses.put(long.class, Long.class);
		wrapperClasses.put(short.class, Short.class);
		wrapperClasses.put(void.class, Void.class);
	}

	private void readyParser(ParameterParser<?> parParser) {
		this.parserMap.put(parParser.getClassParsed(), parParser);
	}

}
