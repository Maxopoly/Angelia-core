package com.github.maxopoly.angeliacore.model.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;

public class AABB {

	private double lowerX;
	private double upperX;
	private double lowerY;
	private double upperY;
	private double lowerZ;
	private double upperZ;

	private List<AABB> innerBoxes;

	public AABB(double lowerX, double upperX, double lowerY, double upperY, double lowerZ, double upperZ) {
		this.lowerX = lowerX;
		this.upperX = upperX;
		this.lowerY = lowerY;
		this.upperY = upperY;
		this.lowerZ = lowerZ;
		this.upperZ = upperZ;
	}

	public AABB(AABB... aabbs) {
		innerBoxes = new ArrayList<>();
		innerBoxes.addAll(Arrays.asList(aabbs));
		innerBoxes.forEach(this::adjustBoundForBox);
	}

	public AABB move(Vector vector) {
		if (innerBoxes == null) {
			return new AABB(lowerX + vector.getX(), upperX + vector.getX(), lowerY + vector.getY(),
					upperY + vector.getY(), lowerZ + vector.getZ(), upperZ + vector.getZ());
		}
		return new AABB(innerBoxes.stream().map(a -> a.move(vector)).toArray(AABB[]::new));
	}

	private void adjustBoundForBox(AABB inner) {
		lowerX = Math.min(lowerX, inner.lowerX);
		upperX = Math.min(upperX, inner.upperX);
		lowerY = Math.min(lowerY, inner.lowerY);
		upperY = Math.min(upperY, inner.upperY);
		lowerZ = Math.min(lowerZ, inner.lowerZ);
		upperZ = Math.min(upperZ, inner.upperZ);
	}

	public boolean intersects(AABB other) {
		if (lowerX > other.upperX || upperX < other.lowerX) {
			return false;
		}
		if (lowerZ > other.upperZ || upperZ < other.lowerZ) {
			return false;
		}
		if (lowerY > other.upperY || upperY < other.lowerY) {
			return false;
		}
		// TODO full implementation of collision checks of sub boxes. How do we do that
		// effieciently
		return true;
	}

	/**
	 * Calculates the first intersection betwen this AABB and the given ray.
	 * Assuming the ray is described as d * t + l, where l is a point in the ray and
	 * d is the direction of the ray, this method returns the smallest non-negative
	 * t for which the ray intersects the AABB. If no intersection is found,
	 * Double.MAX_VALUE is returned
	 * 
	 * @param direction Direction of the ray casted
	 * @param location  A point on the ray
	 * @return Smallest multiple of the direction leading to an intersection or
	 *         Double.MAX_VALUE if no intersection is found
	 */
	public double intersectRay(Vector direction, Location location) {
		double closestMatch = Double.MAX_VALUE;
		if (location.getX() >= upperX) {
			double upperXIntersect = (upperX - location.getX()) / direction.getX();
			Location possibleIntersect = location.add(direction.multiply(upperXIntersect));
			if (isInside(possibleIntersect)) {
				closestMatch = upperXIntersect;
			}
		}
		if (location.getX() <= lowerX) {
			double lowerXIntersect = (lowerX - location.getX()) / direction.getX();
			Location possibleIntersect = location.add(direction.multiply(lowerXIntersect));
			if (isInside(possibleIntersect)) {
				closestMatch = Math.min(closestMatch, lowerXIntersect);
			}
		}
		if (location.getY() <= lowerY) {
			double lowerYIntersect = (lowerY - location.getY()) / direction.getY();
			Location possibleIntersect = location.add(direction.multiply(lowerYIntersect));
			if (isInside(possibleIntersect)) {
				closestMatch = Math.min(closestMatch, lowerYIntersect);
			}
		}
		if (location.getY() >= upperY) {
			double lowerYIntersect = (upperY - location.getY()) / direction.getY();
			Location possibleIntersect = location.add(direction.multiply(lowerYIntersect));
			if (isInside(possibleIntersect)) {
				closestMatch = Math.min(closestMatch, lowerYIntersect);
			}
		}
		if (location.getZ() <= lowerZ) {
			double lowerZIntersect = (lowerZ - location.getY()) / direction.getY();
			Location possibleIntersect = location.add(direction.multiply(lowerZIntersect));
			if (isInside(possibleIntersect)) {
				closestMatch = Math.min(closestMatch, lowerZIntersect);
			}
		}
		if (location.getZ() >= upperZ) {
			double upperZIntersect = (upperZ - location.getY()) / direction.getY();
			Location possibleIntersect = location.add(direction.multiply(upperZIntersect));
			if (isInside(possibleIntersect)) {
				closestMatch = Math.min(closestMatch, upperZIntersect);
			}
		}
		if (innerBoxes == null) {
			return closestMatch;
		}
		double innerMin = Double.MAX_VALUE;
		for (AABB box : innerBoxes) {
			innerMin = Math.min(innerMin, box.intersectRay(direction, location));
		}
		return innerMin;
	}

	public boolean isInside(Location loc) {
		if (loc.getX() < lowerX || loc.getX() > upperX) {
			return false;
		}
		if (loc.getZ() < lowerZ || loc.getZ() > upperZ) {
			return false;
		}
		if (loc.getY() < lowerY || loc.getY() > upperY) {
			return false;
		}
		if (innerBoxes == null) {
			return true;
		}
		for (AABB box : innerBoxes) {
			if (box.isInside(loc)) {
				return true;
			}
		}
		return false;
	}

}
