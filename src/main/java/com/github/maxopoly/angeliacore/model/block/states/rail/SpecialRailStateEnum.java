package com.github.maxopoly.angeliacore.model.block.states.rail;

/**
 * Possible states of a 'special' piece of rail (activator/powered/detector),
 * which can't turn
 *
 */
public enum SpecialRailStateEnum {

	NORTH_SOUTH, EAST_WEST, ASC_EAST, ASC_WEST, ASC_NORTH, ASC_SOUTH;

	public boolean isAscending() {
		return ordinal() >= ASC_EAST.ordinal();
	}

	public boolean isStraight() {
		return ordinal() <= EAST_WEST.ordinal();
	}

}
