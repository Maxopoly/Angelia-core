package com.github.maxopoly.angeliacore.model.location;

public class Vector {

	protected static final double ACCURACY = 1E-9;

	public static Vector calcLocationDifference(Location start, Location target) {
		return target.toVector().subtract(start.toVector());
	}
	protected final double x;
	protected final double y;

	protected final double z;

	public Vector() {
		x = 0;
		y = 0;
		z = 0;
	}

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}
	
	public Vector add(double x, double y, double z) {
		return new Vector(x + this.x, y + this.y, z + this.z);
	}

	public Vector cross(Vector v) {
		return new Vector(v.getX() * x, v.getY() * y, v.getZ() * z);
	}

	public double getLength() {
		return Math.sqrt(x * x + y * y + z * z);
	}

	public Vector getOpposite() {
		return multiply(-1);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public boolean isOrthogonal(Vector other) {
		if (isZero() || other.isZero()) {
			return false;
		}
		return Math.abs(normalize().cross(other.normalize()).getLength()) < ACCURACY;
	}

	public boolean isParallel(Vector other) {
		if (isZero() || other.isZero()) {
			return false;
		}
		return Math.abs(normalize().cross(other.normalize()).getLength() - 1.0) < ACCURACY;
	}

	public boolean isZero() {
		return x == 0.0 && y == 0.0 && z == 0.0;
	}

	public Vector multiply(double factor) {
		return new Vector(x * factor, y * factor, z * factor);
	}

	public Vector normalize() {
		double length = getLength();
		if (length == 0) {
			length = 1;
		}
		return new Vector(x / length, y / length, z / length);
	}

	public Vector subtract(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}

	@Override
	public String toString() {
		return String.format("{%f, %f, %f}", x, y, z);
	}
}
