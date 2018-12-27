package com.github.maxopoly.angeliacore.model.chat;

public class ClickEvent {
	
	public enum Action {
		OPEN_URL, RUN_COMMAND, SUGGEST_COMMAND, CHANGE_PAGE;
	}
	
	private final Action action;
	private final String value;
	
	public ClickEvent(Action action, String value) {
		this.action = action;
		this.value = value;
	}
	
	/**
	 * @return Action to execute if the component is clicked
	 */
	public Action getAction() {
		return action;
	}
	
	/**
	 * @return Value used by the action
	 */
	public String getValue() {
		return value;
	}

}
