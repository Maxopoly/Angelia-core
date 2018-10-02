package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

public class EntityMoveEvent implements AngeliaEvent {

    private LivingEntity entity;

    public EntityMoveEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

}
