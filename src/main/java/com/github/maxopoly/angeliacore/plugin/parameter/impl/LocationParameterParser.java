package com.github.maxopoly.angeliacore.plugin.parameter.impl;

import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.plugin.parameter.InvalidParameterValueException;

public class LocationParameterParser extends AbstractObjectParameterParser<Location> {

	@Override
	public Location parseObject(String value) {
		String[] args = value.split(" ");
		if (args.length != 3) {
			throw new InvalidParameterValueException();
		}
		double[] parsed = new double[3];
		try {
			for (int i = 0; i < 3; i++) {
				parsed[i] = Double.parseDouble(args[i]);
			}
		} catch (NumberFormatException e) {
			throw new InvalidParameterValueException();
		}
		return new Location(parsed[0], parsed[1], parsed[2]);
	}

	@Override
	public Class<Location> getClassParsed() {
		return Location.class;
	}
}
