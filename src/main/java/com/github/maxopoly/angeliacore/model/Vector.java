package com.github.maxopoly.angeliacore.model;

public class Vector {

	private double x;
	private double y;
	private double z;

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

	public Vector multiply(double factor) {
		return new Vector(x * factor, y * factor, z * factor);
	}
}
