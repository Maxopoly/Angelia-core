package com.github.maxopoly.angeliacore.model.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import com.github.maxopoly.angeliacore.model.location.DirectedLocation;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Velocity;
import com.github.maxopoly.angeliacore.model.potion.PotionEffect;

public abstract class LivingEntity extends Entity {

	private UUID uuid;
	protected DirectedLocation location;
	private Map<PotionEffect, Long> potionEffects;
	private float health = 20.f;

	public LivingEntity(int id, UUID uuid, DirectedLocation location, Velocity velocity) {
		// TODO meta data
		super(id, velocity);
		this.uuid = uuid;
		this.location = location;
		this.potionEffects = new HashMap<>();
	}

	public void addPotionEffect(PotionEffect effect) {
		synchronized (potionEffects) {
			potionEffects.remove(effect);
			potionEffects.put(effect, System.currentTimeMillis());
		}
	}

	private void filterTimedOutEffects() {
		synchronized (potionEffects) {
			Set<Entry<PotionEffect, Long>> entrySet = potionEffects.entrySet();
			Iterator<Entry<PotionEffect, Long>> iter = entrySet.iterator();
			while (iter.hasNext()) {
				Entry<PotionEffect, Long> entry = iter.next();
				long start = entry.getValue();
				long runningTime = entry.getKey().getDuration() * 1000L;
				long current = System.currentTimeMillis();
				if ((current - start) > runningTime) {
					iter.remove();
				}
			}
		}
	}

	public List<PotionEffect> getActivePotionEffects() {
		filterTimedOutEffects();
		synchronized (potionEffects) {
			return new LinkedList<>(potionEffects.keySet());
		}
	}

	public float getHealth() {
		return health;
	}

	@Override
	public DirectedLocation getLocation() {
		return location;
	}

	public UUID getUUID() {
		return uuid;
	}

	public void setHealth(float health) {
		this.health = health;
	}

	@Override
	public void updateLocation(Location loc) {
		this.location = new DirectedLocation(loc, location.getYaw(), location.getPitch());
	}

	public void updateLookingDirection(float yaw, float pitch) {
		location = new DirectedLocation(location, yaw, pitch);
	}

}
