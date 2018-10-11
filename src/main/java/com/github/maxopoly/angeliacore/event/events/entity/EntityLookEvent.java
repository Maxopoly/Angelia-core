package com.github.maxopoly.angeliacore.event.events.entity;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;

public class EntityLookEvent implements AngeliaEvent {
	
    private LivingEntity entity;
    private DirectedLocation oldLocation;
    private DirectedLocation newLocation;
    
    public EntityLookEvent(LivingEntity entity, DirectedLocation oldLocation, DirectedLocation newLocation) {
        this.entity = entity;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
    }

    public LivingEntity getEntity() {
        return entity;
    }
    
    public DirectedLocation getOldLookingDirection() {
    	return oldLocation;
    }
    
    public DirectedLocation getNewLookingDirection() {
    	return newLocation;
    }
	

}
