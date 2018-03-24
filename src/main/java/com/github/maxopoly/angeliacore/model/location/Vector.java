package com.github.maxopoly.angeliacore.model.location;

public final class Vector {

	private final double x;
	private final double y;
	private final double z;

	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
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

	public Vector add(Vector other) {
		return new Vector(x + other.x, y + other.y, z + other.z);
	}

	public Vector subtract(Vector other) {
		return new Vector(x - other.x, y - other.y, z - other.z);
	}

	public Vector multiply(double factor) {
		return new Vector(x * factor, y * factor, z * factor);
	}

	public Vector cross(Vector v) {
		return new Vector(v.getX() * x, v.getY() * y, v.getZ() * z);
	}

	public Vector normalize() {
		double length = Math.sqrt(x*x + y*y + z*z);
		if(length == 0) {
			length = 1;
		}
		return new Vector(x/length, y/length, z/length);
	}

	public Vector getOpposite() {
		return multiply(-1);
	}

	public boolean isZero() {
		return x == 0.0 && y == 0.0 && z == 0.0;
	}

	public static Vector calcLocationDifference(Location start, Location target) {
		return target.toVector().subtract(start.toVector());
	}
}
