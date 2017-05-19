package com.github.maxopoly.MineApp.connection.play;

public class PlayerStatus {

	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;
	private int totalEXP;
	private float xpProgress;
	private int level;

	public void updateXP(float progress, int level, int totalXP) {
		this.level = level;
		this.xpProgress = progress;
		this.totalEXP = totalXP;
	}

	public void updatePosition(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void updateLookingDirection(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}

	/**
	 * @return x coordinate of the players position
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return y coordinate of the players position
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return z coordinate of the players position
	 */
	public double getZ() {
		return z;
	}

	/**
	 * @return players current yaw, used to calculate the direction the player is facing
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * @return players current pitch, used to calculate the direction the player is facing
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @return How close the player is to leveling up in a range from 0.0 to 1.0
	 */
	public float getXPProgress() {
		return xpProgress;
	}

	public int getLevel() {
		return level;
	}

	public int getTotalXP() {
		return totalEXP;
	}
}
