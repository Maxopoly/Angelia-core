package com.github.maxopoly.angeliacore.event.events.tablist;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;

/**
 * Called when a players display name in the tablist is changed
 *
 */
public class OtherPlayerDisplayNameChangeEvent implements AngeliaEvent {

	private OnlinePlayer player;
	private String newName;

	public OtherPlayerDisplayNameChangeEvent(OnlinePlayer player, String name) {
		this.player = player;
		this.newName = name;
	}

	/**
	 * @return Player who had his display name changed
	 */
	public OnlinePlayer getPlayer() {
		return player;
	}

	/**
	 * @return Previous display name shown in the tablist for this player. May be
	 *         null
	 */
	public String getPreviousName() {
		return player.getDisplayName();
	}

	/**
	 * @return Updated display name to show in the tablist for this player. May be
	 *         null
	 */
	public String getUpdatedName() {
		return newName;
	}

}