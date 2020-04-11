package com.github.maxopoly.angeliacore.event.events.player;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;

public class PlayerSpawnEvent implements AngeliaEvent {

	private Location loc;
	private OnlinePlayer player;
	private LivingEntity entity;

	public PlayerSpawnEvent(Location loc, OnlinePlayer player, LivingEntity entity) {
		this.loc = loc;
		this.player = player;
		this.entity = entity;
	}

	public LivingEntity getEntity() {
		return entity;
	}

	public Location getLocation() {
		return loc;
	}

	public OnlinePlayer getOnlinePlayer() {
		return player;
	}

}
