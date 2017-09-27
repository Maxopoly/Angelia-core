package com.github.maxopoly.angeliacore.util;

import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.MovementDirection;
import java.util.Iterator;

public class HorizontalField implements Iterable<Location> {

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
			MovementDirection primaryMovementDirection, MovementDirection secondaryMovementDirection, boolean snakeLines,
			int[] sidewardsIncrements) {
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

	public HorizontalField(int lowerX, int upperX, int lowerZ, int upperZ, int y,
			MovementDirection primaryMovementDirection, MovementDirection secondaryMovementDirection, boolean snakeLines,
			int sidewardsIncrement) {
		this(lowerX, upperX, lowerZ, upperZ, y, primaryMovementDirection, secondaryMovementDirection, snakeLines,
				new int[] { sidewardsIncrement });
	}

	/**
	 * Creates a fresh copy with the same values as this instance, but different y level
	 *
	 * @param y
	 * @return
	 */
	public HorizontalField copy(int y) {
		return new HorizontalField(lowerX, upperX, lowerZ, upperZ, y, originalMovementDirection,
				secondaryMovementDirection, snakeLines, sidewardsIncrements);
	}

	/**
	 * The primary and secondary movement direction implicitly define in which corner we start
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
		return new Location(currentX, y, currentZ, 0.0f, 0.0f);
	}

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
		return new Location(currentX, y, currentZ, 0.0f, 0.0f);
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

	@Override
	public Iterator<Location> iterator() {
		// we define the iterator behavior here, so we only have to define the individual method behavior in the
		// subclasses
		return new Iterator<Location>() {

			@Override
			public boolean hasNext() {
				Location curr = new Location(currentX, y, currentZ);
				return !(outsideField(curr.addVector(primaryMovementDirection.toVector())) && outsideField(curr
						.addVector(secondaryMovementDirection.toVector())));
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
		};
	}

	/**
	 * Calculates the direction the next location will be in from the previous one
	 *
	 * @return Current movement direction
	 */
	public MovementDirection getCurrentDirection() {
		Location nextLoc = new Location(currentX + (int) primaryMovementDirection.toVector().getX(), y, currentZ
				+ (int) primaryMovementDirection.toVector().getZ());
		if (sidewardsMovementLeft > 0 || outsideField(nextLoc)) {
			return secondaryMovementDirection;
		}
		return primaryMovementDirection;
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
		}
		throw new IllegalStateException(primaryMovementDirection.name() + " is not a valid movement direction");
	}

	private boolean outsideField(Location loc) {
		return loc.getBlockX() > upperX || loc.getBlockZ() > upperZ || loc.getBlockX() < lowerX || loc.getBlockZ() < lowerZ;
	}

	public int getLowerX() {
		return lowerX;
	}

	public int getUpperX() {
		return upperX;
	}

	public int getLowerZ() {
		return lowerZ;
	}

	public int getUpperZ() {
		return upperZ;
	}

	public int getArea() {
		return Math.abs(lowerX - upperX) * Math.abs(lowerZ - upperZ);
	}

	public int getY() {
		return y;
	}

	public boolean usesSnakelines() {
		return snakeLines;
	}

	public MovementDirection getSecondaryDirection() {
		return secondaryMovementDirection;
	}
}
