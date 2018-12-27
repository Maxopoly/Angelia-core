package com.github.maxopoly.angeliacore.model.chat;

import java.util.LinkedList;
import java.util.List;

import com.github.maxopoly.angeliacore.model.chat.hover.HoverEvent;

/**
 * Displays text which is loaded in the players selected language from a local
 * file with the given identifier name
 *
 */
public class TranslationComponent extends ChatComponent {

	private String identifier;
	private List<ChatComponent> components;

	public TranslationComponent(boolean bold, boolean italic, boolean underlined, boolean strikethrough,
			boolean obfuscated, String insertion, ClickEvent clickEvent, HoverEvent hoverEvent, ChatColor color,
			String identfier, List<ChatComponent> components) {
		super(bold, italic, underlined, strikethrough, obfuscated, insertion, clickEvent, hoverEvent, color);
		this.identifier = identfier;
		this.components = components;
	}

	/**
	 * @return Name of the file to use
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return Components to insert into the loaded text, each component replaces %s
	 *         the same way String.format does
	 */
	public List<ChatComponent> getComponents() {
		return new LinkedList<>(components);
	}

}
