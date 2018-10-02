package com.github.maxopoly.angeliacore.model.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OtherPlayerManager {

	private final Map<UUID, OnlinePlayer> players = new HashMap<>();

	public OnlinePlayer getPlayer(UUID uuid) {
		return players.get(uuid);
	}

	public void removePlayer(UUID uuid) {
		players.remove(uuid);
	}

	public void addPlayer(OnlinePlayer player) {
		players.put(player.getUUID(), player);
	}

}
