package com.github.maxopoly.angeliacore.event.events.tablist;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;

/**
 * Called when a player leaves the game and is removed from the tablist
 *
 */
public class OtherPlayerLeaveGameEvent implements AngeliaEvent {

	private OnlinePlayer player;

	public OtherPlayerLeaveGameEvent(OnlinePlayer player) {
		this.player = player;
	}

	/**
	 * @return Player who left
	 */
	public OnlinePlayer getPlayer() {
		return player;
	}

}
