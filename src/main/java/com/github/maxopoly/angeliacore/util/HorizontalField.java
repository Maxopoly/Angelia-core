package com.github.maxopoly.angeliacore.util;

import java.util.Iterator;

import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.MovementDirection;

/**
 * Utility class for handling rectangular fields. Allows iterating over a field
 * at configurable intervalls
 *
 */
public class HorizontalField implements Iterable<Location> {

	public class FieldIterator implements Iterator<Location> {

		/**
		 * Fast forwards the iterator to the given location. If the given location is
		 * not inside of the field, the iterator will be forwarded until its end and
		 * false will be returned
		 * 
		 * @param loc Location to fast forward to
		 * @return True if the location was found and forwarded to, false if not
		 */
		public boolean fastForward(Location loc) {
			loc = loc.toBlockLocation();
			while (hasNext()) {
				Location curr = next();
				if (curr.equals(loc)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean hasNext() {
			Location curr = new Location(currentX, y, currentZ);
			return !(isOutsideField(new Location(curr.add(primaryMovementDirection.toVector())))
					&& isOutsideField(new Location(curr.add(secondaryMovementDirection.toVector()))));
		}

		@Override
		public Location next() {
			if (sidewardsMovementLeft > 0) {
				currentX += secondaryMovementDirection.toVector().getX();
				currentZ += secondaryMovementDirection.toVector().getZ();
				sidewardsMovementLeft--;
			} else {
				currentX += primaryMovementDirection.toVector().getX();
				currentZ += primaryMovementDirection.toVector().getZ();
				if (pastEnd()) {
					backAndSidewards();
				}
			}
			Location loc = new Location(currentX, y, currentZ);
			return loc;
		}

	}

	private int lowerX;
	private int upperX;
	private int lowerZ;
	private int upperZ;
	private int y;
	private int currentX;
	private int currentZ;
	private boolean snakeLines;
	private MovementDirection originalMovementDirection;
	private MovementDirection primaryMovementDirection;
	private MovementDirection secondaryMovementDirection;
	private int[] sidewardsIncrements;
	private int sidewardIncrementPointer;

	private int sidewardsMovementLeft;

	public HorizontalField(int lowerX, int upperX, int lowerZ, int upperZ, int y,
			MovementDirection primaryMovementDirection, MovementDirection secondaryMovementDirection,
			boolean snakeLines, int sidewardsIncrement) {
		this(lowerX, upperX, lowerZ, upperZ, y, primaryMovementDirection, secondaryMovementDirection, snakeLines,
				new int[] { sidewardsIncrement });
	}

	public HorizontalField(int lowerX, int upperX, int lowerZ, int upperZ, int y,
			MovementDirection primaryMovementDirection, MovementDirection secondaryMovementDirection,
			boolean snakeLines, int[] sidewardsIncrements) {
		this.lowerX = lowerX;
		this.upperX = upperX;
		this.lowerZ = lowerZ;
		this.upperZ = upperZ;
		this.y = y;
		this.snakeLines = snakeLines;
		this.primaryMovementDirection = primaryMovementDirection;
		this.secondaryMovementDirection = secondaryMovementDirection;
		this.originalMovementDirection = primaryMovementDirection;
		this.sidewardsIncrements = sidewardsIncrements;
		this.sidewardIncrementPointer = 0;
		Location startingLoc = getStartingLocation();
		this.currentX = startingLoc.getBlockX();
		this.currentZ = startingLoc.getBlockZ();
	}

	private void backAndSidewards() {
		// at this point we already are one block out, so let's revert that
		currentX -= primaryMovementDirection.toVector().getX();
		currentZ -= primaryMovementDirection.toVector().getZ();
		// add sidewards movement instead
		currentX += secondaryMovementDirection.toVector().getX();
		currentZ += secondaryMovementDirection.toVector().getZ();
		sidewardsMovementLeft = sidewardsIncrements[sidewardIncrementPointer++] - 1;
		if (sidewardIncrementPointer >= sidewardsIncrements.length) {
			sidewardIncrementPointer = 0;
		}
		if (snakeLines) {
			primaryMovementDirection = primaryMovementDirection.getOpposite();
		}
	}

	/**
	 * Creates a fresh copy of this instance with the same values as this instance,
	 * but at a different y level
	 *
	 * @param y New y level
	 * @return Created instance
	 */
	public HorizontalField copy(int y) {
		return new HorizontalField(lowerX, upperX, lowerZ, upperZ, y, originalMovementDirection,
				secondaryMovementDirection, snakeLines, sidewardsIncrements);
	}

	/**
	 * @return Total area of the field in blocks
	 */
	public int getArea() {
		return Math.abs(lowerX - upperX) * Math.abs(lowerZ - upperZ);
	}

	/**
	 * Calculates the direction the next location will be in from the previous one
	 *
	 * @return Current movement direction
	 */
	public MovementDirection getCurrentDirection() {
		Location nextLoc = new Location(currentX + (int) primaryMovementDirection.toVector().getX(), y,
				currentZ + (int) primaryMovementDirection.toVector().getZ());
		if (sidewardsMovementLeft > 0 || isOutsideField(nextLoc)) {
			return secondaryMovementDirection;
		}
		return primaryMovementDirection;
	}

	/**
	 * @return Last location to visit, the opposite corner from which is started
	 */
	public Location getFinalLocation() {
		double currentZ = 0;
		double currentX = 0;
		switch (originalMovementDirection) {
		case NORTH:
			currentZ = lowerZ;
			break;
		case SOUTH:
			currentZ = upperZ;
			break;
		case EAST:
			currentX = upperX;
			break;
		case WEST:
			currentX = lowerX;
			break;
		default:
			throw new IllegalArgumentException("Invalid primary direction " + secondaryMovementDirection.name());
		}
		switch (secondaryMovementDirection) {
		case NORTH:
			currentZ = lowerZ;
			break;
		case SOUTH:
			currentZ = upperZ;
			break;
		case EAST:
			currentX = upperX;
			break;
		case WEST:
			currentX = lowerX;
			break;
		default:
			throw new IllegalArgumentException("Invalid second direction " + secondaryMovementDirection.name());
		}
		return new Location(currentX, y, currentZ);
	}

	/**
	 * @return Lower x bound
	 */
	public int getLowerX() {
		return lowerX;
	}

	/**
	 * @return Lower z bound
	 */
	public int getLowerZ() {
		return lowerZ;
	}

	public MovementDirection getOriginalPrimaryMovementDirection() {
		return originalMovementDirection;
	}

	public MovementDirection getSecondaryDirection() {
		return secondaryMovementDirection;
	}

	/**
	 * The primary and secondary movement direction implicitly define in which
	 * corner we start
	 * 
	 * @return Location at which to start
	 */
	public Location getStartingLocation() {
		double currentZ = 0;
		double currentX = 0;
		switch (originalMovementDirection) {
		case NORTH:
			currentZ = upperZ;
			break;
		case SOUTH:
			currentZ = lowerZ;
			break;
		case EAST:
			currentX = lowerX;
			break;
		case WEST:
			currentX = upperX;
			break;
		default:
			throw new IllegalArgumentException("Invalid primary direction " + secondaryMovementDirection.name());
		}
		switch (secondaryMovementDirection) {
		case NORTH:
			currentZ = upperZ;
			break;
		case SOUTH:
			currentZ = lowerZ;
			break;
		case EAST:
			currentX = lowerX;
			break;
		case WEST:
			currentX = upperX;
			break;
		default:
			throw new IllegalArgumentException("Invalid second direction " + secondaryMovementDirection.name());
		}
		return new Location(currentX, y, currentZ);
	}

	/**
	 * @return Upper x bound
	 */
	public int getUpperX() {
		return upperX;
	}

	/**
	 * @return Upper z bound
	 */
	public int getUpperZ() {
		return upperZ;
	}

	/**
	 * @return Y-level the field is at
	 */
	public int getY() {
		return y;
	}

	/**
	 * Checks whether the given location is at the given side of the field, meaning
	 * a location one block further into the given movement direction than the given
	 * location would be outside of the field, but the given location itself is
	 * still inside
	 * 
	 * @param side Movement direction for which to check, must be a cardinal
	 *             movement and not UP or DOWN
	 * @param loc  Location to check
	 * @return True if the location is at the given side of the field (on the
	 *         inside), false if not
	 */
	public boolean isAtSide(MovementDirection side, Location loc) {
		if (loc.getBlockY() != y) {
			return false;
		}
		switch (side) {
		case NORTH:
			return loc.getBlockZ() == getLowerZ();
		case SOUTH:
			return loc.getBlockZ() == getUpperZ();
		case EAST:
			return loc.getBlockX() == getUpperX();
		case WEST:
			return loc.getBlockX() == getLowerX();
		default:
			// should never happen
			throw new IllegalStateException(side.toString() + " is an illegal movement direction");
		}
	}

	@Override
	public FieldIterator iterator() {
		return new FieldIterator();
	}

	/**
	 * Checks if the given location is outside of the field
	 * 
	 * @param loc Location to check for
	 * @return True if the given location is outside of the field, false if not
	 */
	public boolean isOutsideField(Location loc) {
		return loc.getBlockX() > upperX || loc.getBlockZ() > upperZ || loc.getBlockX() < lowerX
				|| loc.getBlockZ() < lowerZ;
	}

	private boolean pastEnd() {
		switch (primaryMovementDirection) {
		case NORTH:
			return currentZ < lowerZ;
		case SOUTH:
			return currentZ > upperZ;
		case EAST:
			return currentX > upperX;
		case WEST:
			return currentX < lowerX;
		default:
			throw new IllegalStateException(primaryMovementDirection.name() + " is not a valid movement direction");
		}
	}

	public boolean usesSnakelines() {
		return snakeLines;
	}
}
