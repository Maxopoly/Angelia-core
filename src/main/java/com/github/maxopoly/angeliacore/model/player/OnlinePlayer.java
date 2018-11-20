package com.github.maxopoly.angeliacore.model.player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class OnlinePlayer {

	private final Map<String, PlayerProperty> properties;
	private final UUID uuid;
	private final String name;
	private String displayName;
	private GameMode gameMode;
	private int ping;

	public OnlinePlayer(UUID uuid, String name, List<PlayerProperty> properties, GameMode gameMode, int ping,
			String displayName) {
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

	public String toString() {
		//neat for debugging
		return String.format("%s (%s), %s, GM: %s, Ping: %d", name, displayName, uuid.toString(), gameMode.toString(),
				ping);
	}
}
