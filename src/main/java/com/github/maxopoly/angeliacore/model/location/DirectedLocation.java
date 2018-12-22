package com.github.maxopoly.angeliacore.model.location;

/**
 * A location which is directed in a certain direction, commonly used for the
 * orientation of entities/players
 *
 */
public class DirectedLocation extends Location {

	public static float translateAngleFrom256Step(byte step) {
		return (step) * 180f;
	}
	private float yaw;

	private float pitch;

	public DirectedLocation() {
		super();
		yaw = 0f;
		pitch = 0f;
	}

	public DirectedLocation(double x, double y, double z, float yaw, float pitch) {
		super(x, y, z);
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public DirectedLocation(Location loc, float yaw, float pitch) {
		super(loc.x, loc.y, loc.z);
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public DirectedLocation(Vector vec, float yaw, float pitch) {
		super(vec.x, vec.y, vec.z);
		this.yaw = yaw;
		this.pitch = pitch;
	}

	/**
	 * @return current pitch, used to calculate the direction the entity is facing
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @return current yaw, used to calculate the direction the entity is facing
	 */
	public float getYaw() {
		return yaw;
	}

}
