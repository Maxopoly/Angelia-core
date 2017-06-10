package com.github.maxopoly.angeliacore.model;

public enum BlockFace {
	BOTTOM, TOP, NORTH, SOUTH, WEST, EAST, SPECIAL;

	public byte getEnumByte() {
		switch (this) {
			case BOTTOM:
				return 0;
			case EAST:
				return 5;
			case NORTH:
				return 2;
			case SOUTH:
				return 3;
			case SPECIAL:
				// unsigned 255
				return -1;
			case TOP:
				return 1;
			case WEST:
				return 4;
			default:
				return -1;
		}
	}

	/**
	 * Calculates which blockside is the primary one the player is seeing
	 * 
	 * @param playerHeadLoc
	 *          Locations of the players head
	 * @param other
	 *          Relative location, usually the center of a block
	 * @return Side the player is at
	 */
	public static BlockFace getRelative(Location playerHeadLoc, Location other) {
		double xDiff = playerHeadLoc.getX() - other.getX();
		double yDiff = playerHeadLoc.getY() - other.getY();
		double zDiff = playerHeadLoc.getZ() - other.getZ();
		if (Math.abs(xDiff) > Math.abs(yDiff)) {
			if (Math.abs(xDiff) > Math.abs(zDiff)) {
				if (xDiff > 0) {
					return EAST;
				}
				return WEST;
			}
			if (zDiff > 0) {
				return SOUTH;
			}
			return NORTH;
		}
		if (Math.abs(yDiff) > Math.abs(zDiff)) {
			if (yDiff > 0) {
				return TOP;
			}
			return BOTTOM;
		}
		if (zDiff > 0) {
			return SOUTH;
		}
		return NORTH;
	}
}
