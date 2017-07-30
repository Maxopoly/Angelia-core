package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.ActionQueueEmptiedEvent;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class ActionQueue {

	private LinkedList<AbstractAction> actions;
	private ServerConnection connection;

	public ActionQueue(ServerConnection connection) {
		this.actions = new LinkedList<AbstractAction>();
		this.connection = connection;
	}

	public synchronized void queue(AbstractAction action) {
		actions.add(action);
	}

	public synchronized void tick() {
		if (actions.size() == 0) {
			return;
		}
		boolean allDone = false;
		Set<ActionLock> locksTaken = new HashSet<ActionLock>();
		Iterator<AbstractAction> iter = actions.iterator();
		while (!allDone && iter.hasNext()) {
			AbstractAction action = iter.next();
			for (ActionLock lock : action.getActionLocks()) {
				if (locksTaken.contains(lock)) {
					allDone = true;
					break;
				} else {
					locksTaken.add(lock);
				}
			}
			if (allDone) {
				break;
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

	public synchronized void clear() {
		actions.clear();
	}
}
