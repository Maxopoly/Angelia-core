package com.github.maxopoly.angeliacore.model.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OnlinePlayer {

	private Map<String, PlayerProperty> properties;
	private UUID uuid;
	private String name;
	private String displayName;
	private GameMode gameMode;
	private int ping;

	public OnlinePlayer(UUID uuid, String name, List<PlayerProperty> properties, GameMode gameMode, int ping, String displayName) {
		this.uuid = uuid;
		this.name = name;
		this.properties = new HashMap<>();
		for (PlayerProperty prop : properties) {
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

	public void setGameMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

	public GameMode getGameMode() {
		return gameMode;
	}

	public UUID getUUID() {
		return uuid;
	}
}
