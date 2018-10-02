package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

public class EntityLoadEvent implements AngeliaEvent {

    private LivingEntity entity;

    public EntityLoadEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

}
