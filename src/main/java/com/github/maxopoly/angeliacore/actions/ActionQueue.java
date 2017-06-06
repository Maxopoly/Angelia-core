package com.github.maxopoly.angeliacore.actions;

import java.util.LinkedList;
import java.util.Queue;

public class ActionQueue {

	private Queue<AbstractAction> actions;

	public ActionQueue() {
		this.actions = new LinkedList<AbstractAction>();
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
		}
	}

}
