package com.github.maxopoly.angeliacore.event.events.tablist;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;

/**
 * Called when another player joins the server and is added to the tab list
 *
 */
public class OtherPlayerJoinEvent implements AngeliaEvent {

	private OnlinePlayer player;

	public OtherPlayerJoinEvent(OnlinePlayer player) {
		this.player = player;
	}

	/**
	 * @return Player who joined
	 */
	public OnlinePlayer getPlayer() {
		return player;
	}

}
