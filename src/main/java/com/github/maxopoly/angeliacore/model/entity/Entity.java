package com.github.maxopoly.angeliacore.model.entity;

import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;

public abstract class Entity {

	protected int id;
	private Vector velocity;
	private boolean onGround;

	public Entity(int id) {
		this(id, new Vector(0, 0, 0));
	}

	public Entity(int id, Vector velocity) {
		this.id = id;
		this.velocity = velocity;
		this.onGround = true;
	}

	public int getID() {
		return id;
	}

	public abstract Location getLocation();
	
	public abstract AABB getBoundingBox();

	public Vector getVelocity() {
		return velocity;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public void setVelocity(Vector velocity) {
		this.velocity = velocity;
	}

	public abstract void updateLocation(Location loc);

}
