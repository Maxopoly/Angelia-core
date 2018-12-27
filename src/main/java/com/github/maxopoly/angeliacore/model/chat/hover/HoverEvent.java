package com.github.maxopoly.angeliacore.model.chat.hover;

public abstract class HoverEvent {

	public enum Action {
		SHOW_TEXT, SHOW_ITEM, SHOW_ENTITY;
	}
	
	protected final Action action;
	
	public HoverEvent(Action action) {
		this.action = action;
	}
	
	/**
	 * @return Action type enum
	 */
	public Action getAction() {
		return action;
	}
	
}
