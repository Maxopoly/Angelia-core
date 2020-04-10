package com.github.maxopoly.angeliacore.event.events.player;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

public class HungerChangeEvent implements AngeliaEvent {

	private int oldValue;
	private int newValue;

	public HungerChangeEvent(int oldValue, int newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public int getNewValue() {
		return newValue;
	}

	public int getOldValue() {
		return oldValue;
	}
}
