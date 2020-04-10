package com.github.maxopoly.angeliacore.actions;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a chain of actions
 */
public class SequentialActionExecution extends AbstractAction {

	private int currentAction;
	private AbstractAction[] actions;

	public SequentialActionExecution() {
		super(null);
		throw new IllegalArgumentException("Must give at least one action");
	}

	/**
	 * Create the action based on the parameters
	 * 
	 * @param actions
	 */
	public SequentialActionExecution(AbstractAction... actions) {
		super(actions[0].connection);
		this.actions = actions;
		this.currentAction = 0;
	}

	/**
	 * Executes next action in the chain.
	 */
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

	/**
	 * Returns the next action within the chain, without executing it. If no actions
	 * are left in the chain, it returns null
	 * 
	 * @return The next action within the chain.
	 */
	public AbstractAction getNextAction() {
		if (isDone())
			return null;
		return actions[currentAction];
	}

	/**
	 * Return a merge of all the action locks in the remaining actions.
	 * 
	 * @return The action locks or an empty array if none;
	 */
	@Override
	public ActionLock[] getActionLocks() {
		Set<ActionLock> collActions = new HashSet<ActionLock>();
		for (int i = currentAction; i < actions.length; i++) {
			for (ActionLock lock : actions[i].getActionLocks()) {
				collActions.add(lock);
			}
		}
		return collActions.toArray(new ActionLock[0]);
	}

	/**
	 * Return whether the chain is done.
	 */
	@Override
	public boolean isDone() {
		return currentAction >= actions.length;
	}

}
