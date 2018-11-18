package com.github.maxopoly.angeliacore.block.states;

public enum BlockHalfEnum {

	BOTTOM, TOP;
	
	public static BlockHalfEnum parse(String val) {
		switch (val.toLowerCase()) {
		case "bottom":
		case "down":
		case "lower":
			return BOTTOM;
		case "top":
		case "up":
		case "upper":
			return TOP;
		}
		throw new IllegalArgumentException(val + " is not a block half");
	}
	
}
