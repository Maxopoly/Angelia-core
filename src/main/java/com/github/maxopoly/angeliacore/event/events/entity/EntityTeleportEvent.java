package com.github.maxopoly.angeliacore.event.events.entity;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.entity.Entity;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;

public class EntityTeleportEvent implements AngeliaEvent {

	private Entity entity;
	private DirectedLocation oldLocation;
	private DirectedLocation newLocation;

	public EntityTeleportEvent(Entity entity, DirectedLocation oldLocation, DirectedLocation newLocation) {
		this.entity = entity;
		this.oldLocation = oldLocation;
		this.newLocation = newLocation;
	}

	public Entity getEntity() {
		return entity;
	}

	public DirectedLocation getNewLocation() {
		return newLocation;
	}

	public DirectedLocation getOldLocation() {
		return oldLocation;
	}
}