package com.github.maxopoly.angeliacore.model.item;

public enum Hand {

	MAINHAND, OFFHAND;

	public Hand fromInt(int i) {
		switch (i) {
		case 0:
			return MAINHAND;
		case 1:
			return OFFHAND;
		default:
			throw new IllegalArgumentException(i + " is not a valid hand");
		}
	}

	public int toInt() {
		if (this == MAINHAND) {
			return 0;
		}
		return 1;
	}

}
