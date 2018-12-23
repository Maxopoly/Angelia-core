package com.github.maxopoly.angeliacore.event.events.angelia;

import com.github.maxopoly.angeliacore.event.events.AngeliaEvent;

/**
 * Called when the action queue has been completly emtpied. Event call is made
 * within the same client tick as consuming the last element in the queue
 *
 */
public class ActionQueueEmptiedEvent implements AngeliaEvent {

	public ActionQueueEmptiedEvent() {

	}

}
