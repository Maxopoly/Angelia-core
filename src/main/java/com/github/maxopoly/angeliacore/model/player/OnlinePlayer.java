package com.github.maxopoly.angeliacore.model.player;

import com.github.maxopoly.angeliacore.model.entity.EntityProperty;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OnlinePlayer {

	private UUID uuid;
	private String name;
	private String displayName;
	private Map<String, EntityProperty> properties;
	private int gameMode;
	private int ping;
	private LivingEntity entity;

	public OnlinePlayer(UUID uuid, String name, List<EntityProperty> properties, int gameMode, int ping, String displayName) {
		this.uuid = uuid;
		this.name = name;
		this.properties = new HashMap<>();
		for (EntityProperty prop : properties) {
			this.properties.put(prop.getName(), prop);
		}
		this.gameMode = gameMode;
		this.ping = ping;
		this.displayName = displayName;
	}

	public void updatePing(int ping) {
		this.ping = ping;
	}

	public int getPing() {
		return ping;
	}

	public String getName() {
		return name;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setGameMode(int gameMode) {
		this.gameMode = gameMode;
	}

	public int getGameMode() {
		return gameMode;
	}

	public UUID getUUID() {
		return uuid;
	}

	public EntityProperty getProperty(String propName) {
		return properties.get(propName);
	}

	public void declarePlayerEntity(LivingEntity entity) {
		this.entity = entity;
	}

	public LivingEntity getPlayerEntity() {
		return entity;
	}

}
