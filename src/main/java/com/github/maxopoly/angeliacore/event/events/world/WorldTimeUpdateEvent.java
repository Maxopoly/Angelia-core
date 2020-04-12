package com.github.maxopoly.angeliacore.event.events.world;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

public class WorldTimeUpdateEvent implements AngeliaEvent {

	private long oldWorldTime;
	private long newWorldTime;

	public WorldTimeUpdateEvent(long oldWorldTime, long newWorldTime) {
		this.oldWorldTime = oldWorldTime;
		this.newWorldTime = newWorldTime;
	}

	/**
	 * @return World time in seconds before the update, -1 means uninitialized / no value was known before
	 */
	public long getOldWorldTime() {
		return oldWorldTime;
	}
	
	/**
	 * @return Time in seconds which has passed since the worlds creation
	 */
	public long getNewWorldTime() {
		return newWorldTime;
	}

}
