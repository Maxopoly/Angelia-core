package com.github.maxopoly.angeliacore.event.events.world;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

public class DayTimeUpdateEvent implements AngeliaEvent {
	
	private long oldDayTime;
	private long newDayTime;

	public DayTimeUpdateEvent(long oldDayTime, long newDayTime) {
		this.oldDayTime = oldDayTime;
		this.newDayTime = newDayTime;
	}

	/**
	 * @return Day time in ticks before the update, -1 means uninitialized / no value was known before
	 */
	public long getOldWorldTime() {
		return oldDayTime;
	}
	
	/**
	 * @return Updated day time in ticks, usually on a scale from 0 to 23999
	 */
	public long getNewWorldTime() {
		return newDayTime;
	}
}
