package com.github.maxopoly.angeliacore.model.chat;

import java.util.HashMap;
import java.util.Map;

public enum ChatColor {

	BLACK("Black", 0x000000), DARK_BLUE("Dark blue", 0x0000aa), DARK_GREEN("Dark green", 0x00aa00),
	DARK_CYAN("Dark cyan", "dark_aqua", 0x00aaaa), DARK_RED("Dark red", 0xaa0000),
	PURPLE("Purple", "dark_purple", 0xaa00aa), GOLD("Gold", 0xffaa00), GRAY("Gray", 0xaaaaaa),
	DARK_GRAY("Dark gray", 0x555555), BLUE("Blue", 0x5555ff), GREEN("Green", 0x55ff55),
	CYAN("Cyan", "aqua"/* useless */, 0x55ffff), RED("Red", 0xff5555), PINK("Pink", "light_purple", 0xff55ff),
	YELLOW("Yellow", 0xffff55), WHITE("White", 0xffffff), DEFAULT("default", "reset", 0xffffff);

	private static Map<String, ChatColor> mapping = new HashMap<>();

	private int color;
	private String name;
	private String commonName;

	private ChatColor(String commonName, int color) {
		this(commonName, commonName.toLowerCase().replace(" ", "_"), color);
	}

	private ChatColor(String commonName, String name, int color) {
		this.commonName = commonName;
		this.color = color;
		this.name = name;
		insert();
	}

	/**
	 * @return RGB used for this color in the vanilla client
	 */
	public int getRGBColor() {
		return color;
	}

	/**
	 * @return Human friendly name of the color
	 */
	public String getCommonName() {
		return commonName;
	}

	/**
	 * @return Identifier used for the color internally
	 */
	public String getName() {
		return name;
	}

	private void insert() {
		mapping.put(name.toLowerCase(), this);
	}

	public static ChatColor getColor(String identifier) {
		return mapping.get(identifier.toLowerCase());
	}

}
