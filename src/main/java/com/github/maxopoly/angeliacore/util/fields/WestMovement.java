package com.github.maxopoly.angeliacore.util.fields;

import com.github.maxopoly.angeliacore.model.Location;
import java.util.Iterator;

public class WestMovement implements Iterable<Location> {

	private int lowerX;
	private int upperX;
	private int z;
	private int y;
	private int currentX;

	public WestMovement(int lowerX, int upperX, int z, int y) {
		this.lowerX = lowerX;
		this.upperX = upperX;
		this.z = z;
		this.y = y;
		this.currentX = upperX;
	}

	@Override
	public Iterator<Location> iterator() {
		return new Iterator<Location>() {

			@Override
			public boolean hasNext() {
				return currentX >= lowerX;
			}

			@Override
			public Location next() {
				return new Location(currentX--, y, z);
			}
		};
	}
}
