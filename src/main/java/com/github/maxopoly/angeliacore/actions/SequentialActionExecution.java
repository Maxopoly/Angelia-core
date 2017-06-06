package com.github.maxopoly.angeliacore.actions;

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
	public boolean isDone() {
		return currentAction >= actions.length;
	}

}
