package com.github.maxopoly.angeliacore.connection.play;

import java.util.HashMap;
import java.util.Map;

import com.github.maxopoly.angeliacore.model.entity.Entity;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

public class EntityManager {
	
	private Map <Integer, Entity> entities;
	
	public EntityManager() {
		entities = new HashMap<>();
	}
	
	public Entity getEntity(int id) {
		return entities.get(id);
	}
	
	public LivingEntity getLivingEntity(int id) {
		Entity entity = entities.get(id);;
		if (entity instanceof LivingEntity) {
			return (LivingEntity) entity;
		}
		return null;
	}
	
	public void addEntity(Entity entity) {
		entities.put(entity.getID(), entity);
	}
	
	public void removeEntity(int id) {
		entities.remove(id);
	}

}
