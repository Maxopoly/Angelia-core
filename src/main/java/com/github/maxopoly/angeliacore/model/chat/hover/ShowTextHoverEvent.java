package com.github.maxopoly.angeliacore.model.chat.hover;

import java.util.List;

import com.github.maxopoly.angeliacore.model.chat.ChatComponent;

/**
 * Upon hovering components with this hover action, text/chat components are shown
 *
 */
public class ShowTextHoverEvent extends HoverEvent {

	private List<ChatComponent> chatComponents;
	
	public ShowTextHoverEvent(List<ChatComponent> chatComponents) {
		super(Action.SHOW_TEXT);
		this.chatComponents = chatComponents;
	}
	
	/**
	 * @return Chat components to show on hover
	 */
	public List<ChatComponent> getText() {
		return chatComponents;
	}
}
