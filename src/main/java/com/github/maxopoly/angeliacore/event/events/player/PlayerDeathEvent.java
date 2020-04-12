package com.github.maxopoly.angeliacore.event.events.player;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.ThePlayer;

/**
 * Called when the player (the one we are) dies
 *
 */
public class PlayerDeathEvent implements AngeliaEvent {

	private ThePlayer player;

	public PlayerDeathEvent(ThePlayer player) {
		this.player = player;
	}

	/**
	 * @return Player (always the one we are) who died
	 */
	public ThePlayer getPlayer() {
		return player;
	}
}
