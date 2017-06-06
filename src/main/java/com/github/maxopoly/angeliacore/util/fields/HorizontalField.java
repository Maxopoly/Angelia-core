package com.github.maxopoly.angeliacore.util.fields;

import com.github.maxopoly.angeliacore.api.MovementDirection;
import com.github.maxopoly.angeliacore.model.Location;
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
	private MovementDirection primaryMovementDirection;
	private MovementDirection secondaryMovementDirection;
	private Iterator<Location> lineIterator;

	public HorizontalField(int lowerX, int upperX, int lowerZ, int upperZ, int y,
			MovementDirection primaryMovementDirection, MovementDirection secondaryMovementDirection, boolean snakeLines) {
		this.lowerX = lowerX;
		this.upperX = upperX;
		this.lowerZ = lowerZ;
		this.upperZ = upperZ;
		this.y = y;
		this.snakeLines = snakeLines;
		this.currentX = lowerX;
		this.currentZ = lowerZ;
		this.primaryMovementDirection = primaryMovementDirection;
		this.secondaryMovementDirection = secondaryMovementDirection;
		setLineIterator(true);
	}

	public HorizontalField(int lowerX, int upperX, int lowerZ, int upperZ, int y,
			MovementDirection primaryMovementDirection, MovementDirection secondaryMovementDirection) {
		this(lowerX, upperX, lowerZ, upperZ, y, primaryMovementDirection, secondaryMovementDirection, false);
	}

	public void setLineIterator(boolean initial) {
		if (!initial) {
			switch (secondaryMovementDirection) {
				case NORTH:
				case SOUTH:
					currentZ += secondaryMovementDirection.toVector().getZ();
					break;
				case EAST:
				case WEST:
					currentX += secondaryMovementDirection.toVector().getX();
					break;
				default:
					throw new IllegalStateException("Second movement direction must be y = 0 and cardinal direction");
			}
			if (snakeLines) {
				primaryMovementDirection = primaryMovementDirection.getOpposite();
			}
		}
		switch (primaryMovementDirection) {
			case NORTH:
				lineIterator = new NorthMovement(lowerZ, upperZ, currentX, y).iterator();
				break;
			case SOUTH:
				lineIterator = new SouthMovement(lowerZ, upperZ, currentX, y).iterator();
				break;
			case WEST:
				lineIterator = new WestMovement(lowerX, upperX, currentZ, y).iterator();
				break;
			case EAST:
				lineIterator = new EastMovement(lowerX, upperX, currentZ, y).iterator();
				break;
			default:
				throw new IllegalStateException(primaryMovementDirection.name() + " is not a valid direction");
		}
	}

	@Override
	public Iterator<Location> iterator() {
		// we define the iterator behavior here, so we only have to define the individual method behavior in the subclasses
		return new Iterator<Location>() {

			@Override
			public boolean hasNext() {
				if (lineIterator.hasNext()) {
					return true;
				}
				setLineIterator(false);
				if (currentX > upperX && currentZ > upperZ) {
					return false;
				}
				return true;
			}

			@Override
			public Location next() {
				if (lineIterator.hasNext()) {
					return lineIterator.next();
				}
				setLineIterator(false);
				if (currentX > upperX && currentZ > upperZ) {
					return null;
				}
				return lineIterator.next();
			}
		};
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
}
