package com.github.maxopoly.angeliacore.util.fields;

import com.github.maxopoly.angeliacore.model.Location;
import java.util.Iterator;

public class NorthMovement implements Iterable<Location> {

	private int lowerZ;
	private int upperZ;
	private int x;
	private int y;
	private int currentZ;

	public NorthMovement(int lowerZ, int upperZ, int x, int y) {
		this.lowerZ = lowerZ;
		this.upperZ = upperZ;
		this.x = x;
		this.y = y;
		this.currentZ = upperZ;
	}

	@Override
	public Iterator<Location> iterator() {
		return new Iterator<Location>() {

			@Override
			public boolean hasNext() {
				return currentZ >= lowerZ;
			}

			@Override
			public Location next() {
				return new Location(x, y, currentZ--);
			}
		};
	}

}
