package com.github.maxopoly.angeliacore.event.events.player;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

public class SaturationChangeEvent implements AngeliaEvent {

	private float oldValue;
	private float newValue;

	public SaturationChangeEvent(float oldValue, float newValue) {
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public float getNewValue() {
		return newValue;
	}

	public float getOldValue() {
		return oldValue;
	}
}
