package com.github.maxopoly.angeliacore.event.events.entity;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

public class LivingEntityLoadEvent implements AngeliaEvent {

    private LivingEntity entity;

    public LivingEntityLoadEvent(LivingEntity entity) {
        this.entity = entity;
    }

    public LivingEntity getEntity() {
        return entity;
    }

}
