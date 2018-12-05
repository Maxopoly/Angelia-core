package com.github.maxopoly.angeliacore.model.location;

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

	public Vector toVector() {
		switch (this) {
		case BOTTOM:
			return new Vector(0, -0.5, 0.0);
		case EAST:
			return new Vector(0.5, 0.0, 0.0);
		case NORTH:
			return new Vector(0, 0.0, -0.5);
		case SOUTH:
			return new Vector(0, 0.0, 0.5);
		case SPECIAL:
			throw new IllegalAccessError("Cant convert special blockface to vector");
		case TOP:
			return new Vector(0, 0.5, 0.0);
		case WEST:
			return new Vector(-0.5, 0.0, 0.0);
		default:
			throw new IllegalAccessError("Cant convert unknown blockface to vector");
		}
	}

	/**
	 * Calculates which blockside is the primary one the player is seeing
	 * 
	 * @param playerHeadLoc Locations of the players head
	 * @param other         Relative location, usually the center of a block
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

	public static BlockFace parse(String name) {
		switch (name.toLowerCase()) {
		case "top":
		case "up":
			return BlockFace.TOP;
		case "bottom":
		case "down":
			return BlockFace.BOTTOM;
		case "east":
			return BlockFace.EAST;
		case "west":
			return BlockFace.WEST;
		case "north":
			return BlockFace.NORTH;
		case "south":
			return BlockFace.SOUTH;
		case "special":
			return BlockFace.SPECIAL;
		}
		throw new IllegalArgumentException(name + " is not a block face");
	}

	public int placementVectorX() {
		switch (this) {
		case EAST:
			return 15;
		case WEST:
			return 0;
		default:
			return 7;
		}
	}

	public int placementVectorY() {
		switch (this) {
		case TOP:
			return 15;
		case BOTTOM:
			return 0;
		default:
			return 7;
		}
	}

	public int placementVectorZ() {
		switch (this) {
		case SOUTH:
			return 15;
		case NORTH:
			return 0;
		default:
			return 7;
		}
	}
}
