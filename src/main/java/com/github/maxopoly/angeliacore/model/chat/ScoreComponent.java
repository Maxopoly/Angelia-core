package com.github.maxopoly.angeliacore.model.chat;

import com.github.maxopoly.angeliacore.model.chat.hover.HoverEvent;

/**
 * Displays a score
 *
 */
public class ScoreComponent extends ChatComponent {
	
	private String name;
	private String objective;
	private String value;

	public ScoreComponent(boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated,
			String insertion, ClickEvent clickEvent, HoverEvent hoverEvent, ChatColor color, String name, String objective, String value) {
		super(bold, italic, underlined, strikethrough, obfuscated, insertion, clickEvent, hoverEvent, color);
		this.name = name;
		this.objective = objective;
		this.value = value;
	}
	
	/**
	 * @return Player name or entity UUID of the being the score is referring to
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return Name of the objective
	 */
	public String getObjective() {
		return objective;
	}
	
	/**
	 * @return Resolve value of the objective
	 */
	public String getValue() {
		return value;
	}

}
