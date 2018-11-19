package com.github.maxopoly.angeliacore.event.events.tablist;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;

/**
 * Called when another players ping is updated. Vanilla servers will
 * continuously send every players ping to every other player to power the
 * little connection indicators next to the players name in the tab list
 *
 */
public class OtherPlayerPingUpdateEvent implements AngeliaEvent {

	private OnlinePlayer player;
	private int newPing;

	public OtherPlayerPingUpdateEvent(OnlinePlayer player, int ping) {
		this.player = player;
		this.newPing = ping;
	}

	/**
	 * @return Player whose ping is updated
	 */
	public OnlinePlayer getPlayer() {
		return player;
	}

	/**
	 * @return Updated ping of the player
	 */
	public int getNewPing() {
		return newPing;
	}

	/**
	 * @return Last known ping of the player
	 */
	public int getOldPing() {
		return player.getPing();
	}

}