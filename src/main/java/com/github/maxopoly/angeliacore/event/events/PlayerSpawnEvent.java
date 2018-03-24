package com.github.maxopoly.angeliacore.event.events;

import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;

public class PlayerSpawnEvent implements AngeliaEvent {

	private Location loc;
	private OnlinePlayer player;
	private int entityID;

	public PlayerSpawnEvent(Location loc, OnlinePlayer player, int entityID) {
		this.loc = loc;
		this.player = player;
		this.entityID = entityID;
	}

	public Location getLocation() {
		return loc;
	}

	public OnlinePlayer getOnlinePlayer() {
		return player;
	}

	public int getEntityID() {
		return entityID;
	}
}
