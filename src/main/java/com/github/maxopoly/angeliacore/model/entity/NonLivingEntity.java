package com.github.maxopoly.angeliacore.model.entity;

import com.github.maxopoly.angeliacore.model.location.Location;

public class NonLivingEntity extends Entity {

	private Location location;

	public NonLivingEntity(int id, Location location) {
		super(id);
		this.location = location;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public void updateLocation(Location loc) {
		this.location = loc;
	}

	@Override
	public AABB getBoundingBox() {
		// TODO Auto-generated method stub
		return null;
	}
}
