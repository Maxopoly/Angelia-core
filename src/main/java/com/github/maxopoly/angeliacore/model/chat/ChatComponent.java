package com.github.maxopoly.angeliacore.model.chat;

import com.github.maxopoly.angeliacore.model.chat.hover.HoverEvent;

/**
 * Parent class for any object that can be displayed in chat
 *
 */
public abstract class ChatComponent {

	private boolean bold;
	private boolean italic;
	private boolean underlined;
	private boolean strikethrough;
	private boolean obfuscated;
	private String insertion;
	private ClickEvent clickEvent;
	private HoverEvent hoverEvent;
	private ChatColor color;

	public ChatComponent(boolean bold, boolean italic, boolean underlined, boolean strikethrough, boolean obfuscated,
			String insertion, ClickEvent clickEvent, HoverEvent hoverEvent, ChatColor color) {
		this.bold = bold;
		this.italic = italic;
		this.underlined = underlined;
		this.strikethrough = strikethrough;
		this.obfuscated = obfuscated;
		this.insertion = insertion;
		this.clickEvent = clickEvent;
		this.hoverEvent = hoverEvent;
		this.color = color;
	}

	/**
	 * @return Is the text bold
	 */
	public boolean isBold() {
		return bold;
	}

	/**
	 * @return Is the text italic
	 */
	public boolean isItalic() {
		return italic;
	}

	/**
	 * @return Is the text striked through
	 */
	public boolean isStrikeThrough() {
		return strikethrough;
	}

	/**
	 * @return Is the text obfuscated (quicky switching through random characters in
	 *         the vanilla client)
	 */
	public boolean isObfuscated() {
		return obfuscated;
	}

	/**
	 * @return Is the text underlined
	 */
	public boolean isUnderlined() {
		return underlined;
	}

	/**
	 * @return Text that is inserted if the message is shift-clicked. May be null
	 */
	public String getInsertionString() {
		return insertion;
	}

	/**
	 * @return Has text that should be inserted if the message is shift-clicked
	 */
	public boolean hasInsertionString() {
		return insertion != null;
	}

	/**
	 * @return Has something that should be done if the component is clicked
	 */
	public boolean hasClickEvent() {
		return clickEvent != null;
	}

	/**
	 * @return Event to execute if the component is clicked, may be null if none
	 */
	public ClickEvent getClickEvent() {
		return clickEvent;
	}

	/**
	 * @return Has something that should be done if the component is hovered with
	 *         the players cursor
	 */
	public boolean hasHoverEvent() {
		return hoverEvent != null;
	}

	/**
	 * @return Event to execute if the component is hovered by the players cursor,
	 *         may be null if none
	 */
	public HoverEvent getHoverEvent() {
		return hoverEvent;
	}

	/**
	 * @return Color to use
	 */
	public ChatColor getColor() {
		return color;
	}

}
