package com.github.maxopoly.angeliacore.model.entity;

import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Velocity;

public abstract class Entity {

	protected int id;
	private Velocity velocity;
	private boolean onGround;

	public Entity(int id) {
		this(id, new Velocity(0, 0, 0));
	}

	public Entity(int id, Velocity velocity) {
		this.id = id;
		this.velocity = velocity;
		this.onGround = true;
	}

	public int getID() {
		return id;
	}

	public abstract Location getLocation();

	public Velocity getVelocity() {
		return velocity;
	}

	public boolean isOnGround() {
		return onGround;
	}

	public void setOnGround(boolean onGround) {
		this.onGround = onGround;
	}

	public void setVelocity(Velocity velocity) {
		this.velocity = velocity;
	}

	public abstract void updateLocation(Location loc);

}
