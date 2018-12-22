package com.github.maxopoly.angeliacore.model.location;

public class Velocity {

	public static Vector calcLocationDifference(Location start, Location target) {
		return target.toVector().subtract(start.toVector());
	}
	private final short x;
	private final short y;

	private final short z;

	public Velocity() {
		this.x = 0;
		this.y = 0;
		this.z = 0;
	}

	public Velocity(int x, int y, int z) {
		this((short) x, (short) y, (short) z);
	}

	public Velocity(short x, short y, short z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Velocity add(Velocity other) {
		return new Velocity(x + other.x, y + other.y, z + other.z);
	}

	public Velocity cross(Velocity v) {
		return new Velocity(v.getX() * x, v.getY() * y, v.getZ() * z);
	}

	public int getLength() {
		return (int) Math.sqrt(x * x + y * y + z * z);
	}

	public Velocity getOpposite() {
		return multiply((short) -1);
	}

	public short getX() {
		return x;
	}

	public short getY() {
		return y;
	}

	public short getZ() {
		return z;
	}

	public boolean isOrthogonal(Velocity other) {
		if (isZero() || other.isZero()) {
			return false;
		}
		return cross(other).getLength() == 0;
	}

	public boolean isZero() {
		return x == 0 && y == 0 && z == 0;
	}

	public Velocity multiply(short factor) {
		return new Velocity(x * factor, y * factor, z * factor);
	}

	public Velocity subtract(Velocity other) {
		return new Velocity(x - other.x, y - other.y, z - other.z);
	}

	@Override
	public String toString() {
		return String.format("{%d, %d, %d}", x, y, z);
	}

}
