package com.github.maxopoly.angeliacore.model;

import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;
import java.text.DecimalFormat;

public class PlayerStatus {

	public static final double headDelta = 1.62; // how far the head is above the player location

	private boolean initialized = false;

	private Location location;
	private PlayerInventory inventory;
	private int selectedHotbarSlot;

	private int totalEXP;
	private float xpProgress;
	private int level;

	private float health;
	private int hunger;
	private float saturation;

	private DecimalFormat format;

	public PlayerStatus() {
		this.location = new Location(0, 0, 0);
		this.format = new DecimalFormat("#.##");
		this.inventory = new PlayerInventory();
	}

	public void updateXP(float progress, int level, int totalXP) {
		this.level = level;
		this.xpProgress = progress;
		this.totalEXP = totalXP;
	}

	public void updateLocation(Location loc) {
		this.location = loc;
		initialized = true;
	}

	public void updatePosition(double x, double y, double z) {
		this.location = new Location(x, y, z, location.getYaw(), location.getPitch());
		initialized = true;
	}

	public void updateLookingDirection(float yaw, float pitch) {
		this.location = new Location(location.getX(), location.getY(), location.getZ(), yaw, pitch);
	}

	public void updateHealth(float health, int hunger, float saturation) {
		this.health = health;
		this.hunger = hunger;
		this.saturation = saturation;
	}

	/**
	 * @return Player location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return How close the player is to leveling up in a range from 0.0 to 1.0
	 */
	public float getXPProgress() {
		return xpProgress;
	}

	/**
	 * @return Current player level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return Total amount of XP units the player has
	 */
	public int getTotalXP() {
		return totalEXP;
	}

	/**
	 * @return Player health
	 */
	public float getHealth() {
		return health;
	}

	/**
	 * @return Player hunger
	 */
	public int getHunger() {
		return hunger;
	}

	public Location getHeadLocation() {
		return new Location(location.getX(), location.getY() + headDelta, location.getZ(), location.getYaw(),
				location.getPitch());
	}

	/**
	 * @return Whether the location was initialized yet
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/**
	 * @return Players inventory
	 */
	public PlayerInventory getInventory() {
		return inventory;
	}

	/**
	 * @return Slot the player has selected
	 */
	public int getSelectedHotbarSlot() {
		return selectedHotbarSlot;
	}

	public void setSelectedHotbarSlot(int slot) {
		if (slot < 0 || slot > 8) {
			throw new IllegalArgumentException("Slot must be in [0-8]");
		}
		this.selectedHotbarSlot = slot;
	}

	/**
	 * @return Players hunger saturation
	 */
	public float getSaturation() {
		return saturation;
	}

	public String getLocationString() {
		return location.toString();
	}

	public String getHealthString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Health: " + format.format(health));
		sb.append(", Hunger: " + hunger);
		sb.append(", Saturation: " + format.format(saturation));
		return sb.toString();
	}

	public String getXPString() {
		return "Level: " + level + ", totalXP: " + totalEXP + ", progress: " + format.format(xpProgress);
	}

	@Override
	public String toString() {
		return getLocationString() + " ; " + getHealthString() + " ; " + getXPString();
	}
}
