package com.github.maxopoly.angeliacore.event.events.player;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

public class ConnectedToServerEvent implements AngeliaEvent {
	private int playerEntityID;
	private byte gameMode;
	private int dimension;
	private byte difficulty;
	private byte maxPlayers;
	private String lvlType;
	private boolean debugInfo;

	public int getPlayerEntityID() {
		return playerEntityID;
	}

	public byte getGameMode() {
		return gameMode;
	}

	public int getDimension() {
		return dimension;
	}

	public byte getDifficulty() {
		return difficulty;
	}

	public byte getMaxPlayers() {
		return maxPlayers;
	}

	public String getLvlType() {
		return lvlType;
	}

	public boolean isDebugInfo() {
		return debugInfo;
	}

	public ConnectedToServerEvent(int playerEntityID, byte gameMode, int dimension, byte difficulty, byte maxPlayers,
			String lvlType, boolean debugInfo) {
		super();
		this.playerEntityID = playerEntityID;
		this.gameMode = gameMode;
		this.dimension = dimension;
		this.difficulty = difficulty;
		this.maxPlayers = maxPlayers;
		this.lvlType = lvlType;
		this.debugInfo = debugInfo;
	}

}
