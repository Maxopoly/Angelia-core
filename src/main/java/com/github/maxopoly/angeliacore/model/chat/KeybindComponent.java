package com.github.maxopoly.angeliacore.model.chat;

import com.github.maxopoly.angeliacore.model.chat.hover.HoverEvent;

/**
 * Displays the client's current keybind for the specified key
 *
 */
public class KeybindComponent extends ChatComponent {

	private String keyIndentifier;

	public KeybindComponent(boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated,
			String insertion, ClickEvent clickEvent, HoverEvent hoverEvent, ChatColor color, String keyIdentifier) {
		super(bold, italic, underlined, strikethrough, obfuscated, insertion, clickEvent, hoverEvent, color);
		this.keyIndentifier = keyIdentifier;
	}

	/**
	 * @return Minecraft internal identifier for the key, like "key_key.forward", as
	 *         specified in the options.txt of vanilla clients
	 */
	public String getKeybindIdentifier() {
		return keyIndentifier;
	}

}
