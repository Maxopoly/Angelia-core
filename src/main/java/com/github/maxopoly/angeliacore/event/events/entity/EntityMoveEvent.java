package com.github.maxopoly.angeliacore.event.events.entity;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.entity.Entity;
import com.github.maxopoly.angeliacore.model.location.Location;

public class EntityMoveEvent implements AngeliaEvent {

	private Entity entity;
	private Location oldLocation;
	private Location newLocation;

	public EntityMoveEvent(Entity entity, Location oldLocation, Location newLocation) {
		this.entity = entity;
		this.oldLocation = oldLocation;
		this.newLocation = newLocation;
	}

	public Entity getEntity() {
		return entity;
	}

	public Location getNewLocation() {
		return newLocation;
	}

	public Location getOldLocation() {
		return oldLocation;
	}

}
