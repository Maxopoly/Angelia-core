package com.github.maxopoly.angeliacore.model.chat;

import com.github.maxopoly.angeliacore.model.chat.hover.HoverEvent;

/**
 * Normal text to display in chat
 *
 */
public class TextComponent extends ChatComponent {
	
	private String text;

	public TextComponent(boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated,
			String insertion, ClickEvent clickEvent, HoverEvent hoverEvent, ChatColor color, String text) {
		super(bold, italic, underlined, strikethrough, obfuscated, insertion, clickEvent, hoverEvent, color);
		this.text = text;
	}
	
	/**
	 * @return Text to display
	 */
	public String getText() {
		return text;
	}
}
