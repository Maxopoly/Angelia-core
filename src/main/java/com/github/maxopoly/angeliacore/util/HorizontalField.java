package com.github.maxopoly.angeliacore.util;

import com.github.maxopoly.angeliacore.model.Location;
import com.github.maxopoly.angeliacore.model.MovementDirection;
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
	private int sidewardsIncrement;
	private int sidewardsMovementLeft;

	public HorizontalField(int lowerX, int upperX, int lowerZ, int upperZ, int y,
			MovementDirection primaryMovementDirection, MovementDirection secondaryMovementDirection, boolean snakeLines,
			int sidewardsIncrement) {
		this.lowerX = lowerX;
		this.upperX = upperX;
		this.lowerZ = lowerZ;
		this.upperZ = upperZ;
		this.y = y;
		this.snakeLines = snakeLines;
		this.primaryMovementDirection = primaryMovementDirection;
		this.secondaryMovementDirection = secondaryMovementDirection;
		this.originalMovementDirection = primaryMovementDirection;
		this.sidewardsIncrement = sidewardsIncrement;
		Location startingLoc = getStartingLocation();
		this.currentX = startingLoc.getBlockX();
		this.currentZ = startingLoc.getBlockZ();
	}

	public HorizontalField(int lowerX, int upperX, int lowerZ, int upperZ, int y,
			MovementDirection primaryMovementDirection, MovementDirection secondaryMovementDirection) {
		this(lowerX, upperX, lowerZ, upperZ, y, primaryMovementDirection, secondaryMovementDirection, false, 1);
	}

	/**
	 * Creates a fresh copy with the same values as this instance, but different y level
	 * 
	 * @param y
	 * @return
	 */
	public HorizontalField copy(int y) {
		return new HorizontalField(lowerX, upperX, lowerZ, upperZ, y, originalMovementDirection,
				secondaryMovementDirection, snakeLines, sidewardsIncrement);
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

	private void backAndSidewards() {
		// at this point we already are one block out, so let's revert that
		currentX -= primaryMovementDirection.toVector().getX();
		currentZ -= primaryMovementDirection.toVector().getZ();
		// add sidewards movement instead
		currentX += secondaryMovementDirection.toVector().getX();
		currentZ += secondaryMovementDirection.toVector().getZ();
		sidewardsMovementLeft = sidewardsIncrement - 1;
		if (snakeLines) {
			primaryMovementDirection = primaryMovementDirection.getOpposite();
		}
	}

	@Override
	public Iterator<Location> iterator() {
		// we define the iterator behavior here, so we only have to define the individual method behavior in the subclasses
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

	public MovementDirection getCurrentDirection() {
		if (sidewardsMovementLeft > 0) {
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
