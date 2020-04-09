package com.github.maxopoly.angeliacore.model.block.states;

public enum StairShapeEnum {

	STRAIGHT, OUTER_RIGHT, OUTER_LEFT, INNER_RIGHT, INNER_LEFT;

	public static StairShapeEnum parse(String key) {
		switch (key.toLowerCase()) {
		case "straight":
			return STRAIGHT;
		case "outer_right":
			return OUTER_RIGHT;
		case "outer_left":
			return OUTER_LEFT;
		case "inner_right":
			return INNER_RIGHT;
		case "inner_left":
			return INNER_LEFT;
		}
		throw new IllegalArgumentException(key + " is not a stair shape");
	}

}
