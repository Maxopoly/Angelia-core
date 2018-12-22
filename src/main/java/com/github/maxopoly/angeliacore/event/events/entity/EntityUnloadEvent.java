package com.github.maxopoly.angeliacore.event.events.entity;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.entity.Entity;

public class EntityUnloadEvent implements AngeliaEvent {

	private Entity entity;

	public EntityUnloadEvent(Entity entity) {
		this.entity = entity;
	}

	public Entity getEntity() {
		return entity;
	}

}
