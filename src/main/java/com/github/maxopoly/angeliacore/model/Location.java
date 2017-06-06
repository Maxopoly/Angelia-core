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
	 * Drops yaw and pitch and rounds all coordinates to integers
	 * 
	 * @return
	 */
	public Location toBlockLocation() {
		return new Location((int) x, (int) y, (int) z);
	}

	/**
	 * If this location represents a block (using full integer coords), then this method will return the center of the
	 * block
	 * 
	 * @return Block center
	 */
	public Location getBlockCenter() {
		return new Location(x + 0.5, y + 0.5, z + 0.5, yaw, pitch);
	}

	/**
	 * If this location represents a block (using full integer coords), then this method will return the center of the
	 * block, while leaving y unchanged
	 * 
	 * @return Block center only for the x and z coordinate
	 */
	public Location getBlockCenterXZ() {
		return new Location(x + 0.5, y, z + 0.5, yaw, pitch);
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
