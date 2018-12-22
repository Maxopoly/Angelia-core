package com.github.maxopoly.angeliacore.model.location;

import java.util.Arrays;
import java.util.List;

/**
 * Models all 26 directions an entity can move in
 *
 */
public enum MovementDirection {

	NORTH, SOUTH, WEST, EAST, NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST, UP, DOWN, NORTH_UP, SOUTH_UP, WEST_UP,
	EAST_UP, NORTH_WEST_UP, NORTH_EAST_UP, SOUTH_WEST_UP, SOUTH_EAST_UP, NORTH_DOWN, SOUTH_DOWN, WEST_DOWN, EAST_DOWN,
	NORTH_WEST_DOWN, NORTH_EAST_DOWN, SOUTH_WEST_DOWN, SOUTH_EAST_DOWN;

	public static final List<MovementDirection> CARDINAL = Arrays
			.asList(new MovementDirection[] { NORTH, SOUTH, WEST, EAST });

	public static MovementDirection fromVector(Vector v) {
		if (v.getX() > 0) {
			if (v.getY() > 0) {
				if (v.getZ() > 0) {
					return SOUTH_EAST_UP;
				}
				if (v.getZ() == 0) {
					return EAST_UP;
				}
				return NORTH_EAST_UP;
			}
			if (v.getY() == 0) {
				if (v.getZ() > 0) {
					return SOUTH_EAST;
				}
				if (v.getZ() == 0) {
					return EAST;
				}
				return NORTH_EAST;
			}
			if (v.getZ() > 0) {
				return SOUTH_EAST_DOWN;
			}
			if (v.getZ() == 0) {
				return EAST_DOWN;
			}
			return NORTH_EAST_DOWN;
		}
		if (v.getX() == 0) {
			if (v.getY() > 0) {
				if (v.getZ() > 0) {
					return SOUTH_UP;
				}
				if (v.getZ() == 0) {
					return UP;
				}
				return NORTH_UP;
			}
			if (v.getY() == 0) {
				if (v.getZ() > 0) {
					return SOUTH;
				}
				if (v.getZ() == 0) {
					throw new IllegalArgumentException("0,0,0 can't be turned into a direction");
				}
				return NORTH;
			}
			if (v.getZ() > 0) {
				return SOUTH_DOWN;
			}
			if (v.getZ() == 0) {
				return DOWN;
			}
			return NORTH_DOWN;
		}
		if (v.getY() > 0) {
			if (v.getZ() > 0) {
				return SOUTH_WEST_UP;
			}
			if (v.getZ() == 0) {
				return WEST_UP;
			}
			return NORTH_WEST_UP;
		}
		if (v.getY() == 0) {
			if (v.getZ() > 0) {
				return SOUTH_WEST;
			}
			if (v.getZ() == 0) {
				return WEST;
			}
			return NORTH_WEST;
		}
		if (v.getZ() > 0) {
			return SOUTH_WEST_DOWN;
		}
		if (v.getZ() == 0) {
			return WEST_DOWN;
		}
		return NORTH_WEST_DOWN;
	}

	public MovementDirection getOpposite() {
		return MovementDirection.fromVector(this.toVector().multiply(-1));
	}

	public BlockFace toBlockFace() {
		switch (this) {
		case UP:
			return BlockFace.TOP;
		case DOWN:
			return BlockFace.BOTTOM;
		case NORTH:
			return BlockFace.NORTH;
		case WEST:
			return BlockFace.WEST;
		case EAST:
			return BlockFace.EAST;
		case SOUTH:
			return BlockFace.SOUTH;
		default:
			throw new IllegalArgumentException(this.name() + " cant be converted to BlockFace");
		}
	}

	public Vector toVector() {
		switch (this) {
		case NORTH:
			return new Vector(0, 0, -1);
		case SOUTH:
			return new Vector(0, 0, 1);
		case WEST:
			return new Vector(-1, 0, 0);
		case EAST:
			return new Vector(1, 0, 0);
		case NORTH_WEST:
			return new Vector(-1, 0, -1);
		case NORTH_EAST:
			return new Vector(1, 0, -1);
		case SOUTH_WEST:
			return new Vector(-1, 0, 1);
		case SOUTH_EAST:
			return new Vector(1, 0, 1);
		case UP:
			return new Vector(0, 1, 0);
		case DOWN:
			return new Vector(0, -1, 0);
		case NORTH_UP:
			return new Vector(0, 1, -1);
		case SOUTH_UP:
			return new Vector(0, 1, 1);
		case WEST_UP:
			return new Vector(-1, 1, 0);
		case EAST_UP:
			return new Vector(1, 1, 0);
		case NORTH_WEST_UP:
			return new Vector(-1, 1, -1);
		case NORTH_EAST_UP:
			return new Vector(1, 1, -1);
		case SOUTH_WEST_UP:
			return new Vector(-1, 1, 1);
		case SOUTH_EAST_UP:
			return new Vector(1, 1, 1);
		case NORTH_DOWN:
			return new Vector(0, -1, -1);
		case SOUTH_DOWN:
			return new Vector(0, -1, 1);
		case WEST_DOWN:
			return new Vector(-1, -1, 0);
		case EAST_DOWN:
			return new Vector(1, -1, 0);
		case NORTH_WEST_DOWN:
			return new Vector(-1, -1, -1);
		case NORTH_EAST_DOWN:
			return new Vector(1, -1, -1);
		case SOUTH_WEST_DOWN:
			return new Vector(-1, -1, 1);
		case SOUTH_EAST_DOWN:
			return new Vector(1, -1, 1);
		default:
			// this will never happen and is only needed so this compiles
			return new Vector(1, 0, 0);
		}
	}
}
