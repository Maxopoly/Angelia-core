package com.github.maxopoly.angeliacore.model;

import java.text.DecimalFormat;

public class Location {

	private static DecimalFormat format = new DecimalFormat("#.##");

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public Location(double x, double y, double z, float yaw, float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Location(int x, int y, int z) {
		this(x, y, z, 0.0f, 0.0f);
	}

	/**
	 * @return x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return z coordinate
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @return current yaw, used to calculate the direction the entity is facing
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * @return current pitch, used to calculate the direction the entity is facing
	 */
	public float getPitch() {
		return pitch;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Loc: " + format.format(x) + " " + format.format(y) + " " + format.format(z));
		sb.append(", Yaw: " + format.format(yaw));
		sb.append(", Pitch: " + format.format(pitch));
		return sb.toString();
	}

}
