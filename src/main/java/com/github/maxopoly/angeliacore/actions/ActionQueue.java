package com.github.maxopoly.angeliacore.actions;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.angelia.ActionQueueEmptiedEvent;
import com.github.maxopoly.angeliacore.event.events.angelia.AngeliaTickEvent;

public class ActionQueue {

	private LinkedList<AbstractAction> actions;
	private ServerConnection connection;

	public ActionQueue(ServerConnection connection) {
		this.actions = new LinkedList<>();
		this.connection = connection;
	}

	public synchronized void clear() {
		actions.clear();
	}

	public synchronized void queue(AbstractAction action) {
		actions.add(action);
	}

	public synchronized void tick() {
		connection.getEventHandler()
				.broadcast(new AngeliaTickEvent(connection.getIncomingPlayPacketHandler().getTickCounter()));
		if (actions.size() == 0) {
			return;
		}
		boolean allDone = false;
		Set<ActionLock> locksTaken = new HashSet<>();
		Iterator<AbstractAction> iter = actions.iterator();
		outer: while (!allDone && iter.hasNext()) {
			AbstractAction action = iter.next();
			for (ActionLock lock : action.getActionLocks()) {
				if (locksTaken.contains(lock)) {
					allDone = true;
					break outer;
				} else {
					locksTaken.add(lock);
					if (lock == ActionLock.EVERYTHING) {
						allDone = true;
					}
				}
			}
			action.execute();
			if (action.isDone()) {
				iter.remove();
			}
			if (actions.size() == 0) {
				connection.getEventHandler().broadcast(new ActionQueueEmptiedEvent());
				allDone = true;
			}
		}
	}
}
