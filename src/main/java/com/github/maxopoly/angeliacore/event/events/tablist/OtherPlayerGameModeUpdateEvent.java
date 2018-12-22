package com.github.maxopoly.angeliacore.event.events.tablist;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;
import com.github.maxopoly.angeliacore.model.player.GameMode;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;

/**
 * Called when another players game mode is changed. This is called even if the
 * other player is not in render distance and the vanilla client only uses it
 * for the tab list
 *
 */
public class OtherPlayerGameModeUpdateEvent implements AngeliaEvent {

	private OnlinePlayer player;
	private GameMode newGameMode;

	public OtherPlayerGameModeUpdateEvent(OnlinePlayer player, GameMode gameMode) {
		this.player = player;
		this.newGameMode = gameMode;
	}

	/**
	 * @return Gamemode to which the player changed
	 */
	public GameMode getNewGameMode() {
		return newGameMode;
	}

	/**
	 * @return Gamemode the player previously had
	 */
	public GameMode getOldGameMode() {
		return player.getGameMode();
	}

	/**
	 * @return Player whose game mode changed
	 */
	public OnlinePlayer getPlayer() {
		return player;
	}

}
