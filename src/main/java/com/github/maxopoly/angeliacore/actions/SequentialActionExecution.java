package com.github.maxopoly.angeliacore.actions;

import java.util.HashSet;
import java.util.Set;

public class SequentialActionExecution extends AbstractAction {

	private int currentAction;
	private AbstractAction[] actions;

	public SequentialActionExecution() {
		super(null);
		throw new IllegalArgumentException("Must give at least one action");
	}

	public SequentialActionExecution(AbstractAction... actions) {
		super(actions[0].connection);
		this.actions = actions;
		this.currentAction = 0;
	}

	@Override
	public void execute() {
		if (isDone()) {
			return;
		}
		actions[currentAction].execute();
		if (actions[currentAction].isDone()) {
			currentAction++;
		}
	}

	@Override
	public ActionLock[] getActionLocks() {
		Set<ActionLock> collActions = new HashSet<>();
		for (int i = currentAction; i < actions.length; i++) {
			for (ActionLock lock : actions[i].getActionLocks()) {
				collActions.add(lock);
			}
		}
		return collActions.toArray(new ActionLock[0]);
	}

	@Override
	public boolean isDone() {
		return currentAction >= actions.length;
	}

}
