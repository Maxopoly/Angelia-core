package com.github.maxopoly.angeliacore.actions;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.ActionQueueEmptiedEvent;
import java.util.LinkedList;
import java.util.Queue;

public class ActionQueue {

	private Queue<AbstractAction> actions;
	private ServerConnection connection;

	public ActionQueue(ServerConnection connection) {
		this.actions = new LinkedList<AbstractAction>();
		this.connection = connection;
	}

	public void queue(AbstractAction action) {
		actions.add(action);
	}

	public void tick() {
		if (actions.size() == 0) {
			return;
		}
		AbstractAction action = actions.peek();
		action.execute();
		if (action.isDone()) {
			actions.poll();
			if (actions.size() == 0) {
				connection.getEventHandler().broadcast(new ActionQueueEmptiedEvent());
			}
		}
	}
}
