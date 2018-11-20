package com.github.maxopoly.angeliacore.event.events.angelia;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

/**
 * Called whenever the action queue is ticked
 *
 */
public class AngeliaTickEvent implements AngeliaEvent {

	private long tickCounter;

	public AngeliaTickEvent(long tickCounter) {
		this.tickCounter = tickCounter;
	}

	public long getTickCounter() {
		return tickCounter;
	}

}
