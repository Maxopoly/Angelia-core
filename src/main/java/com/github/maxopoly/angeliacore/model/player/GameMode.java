package com.github.maxopoly.angeliacore.model.player;

public enum GameMode {

	SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR, UNKNOWN;

	public static GameMode parse(int value) {
		switch (value) {
		case 0:
			return SURVIVAL;
		case 1:
			return CREATIVE;
		case 2:
			return ADVENTURE;
		case 3:
			return SPECTATOR;
		default:
			return UNKNOWN;
		}
	}

}
