package com.github.maxopoly.angeliacore.block.states.rail;

/**
 * Possible states of a normal piece of rail
 *
 */
public enum RailStateEnum {
	
	NORTH_SOUTH, EAST_WEST, ASC_EAST, ASC_WEST, ASC_NORTH, ASC_SOUTH, SOUTH_EAST, SOUTH_WEST, NORTH_WEST, NORTH_EAST;
	
	public boolean isTurn() {
		return ordinal() >= SOUTH_EAST.ordinal(); 
	}
	
	public boolean isStraight() {
		return ordinal() <= EAST_WEST.ordinal();
	}
	
	public boolean isAscending() {
		int ord = ordinal();
		return ord > EAST_WEST.ordinal() && ord < SOUTH_EAST.ordinal();
	}
	

}
