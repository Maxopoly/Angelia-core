package com.github.maxopoly.angeliacore.model;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.HealthChangeEvent;
import com.github.maxopoly.angeliacore.event.events.HungerChangeEvent;
import com.github.maxopoly.angeliacore.event.events.XPChangeEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;
import com.github.maxopoly.angeliacore.model.location.DirectedLocation;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.location.Vector;
import com.github.maxopoly.angeliacore.model.location.Velocity;
import com.github.maxopoly.angeliacore.model.potion.PotionEffect;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class ThePlayer extends LivingEntity {

	public static final double headDelta = 1.62; // how far the head is above the player location
	private static final Vector headOffSet = new Vector(0, headDelta, 0);

	private boolean initialized = false;
	private Map<Byte, Inventory> openInventories;

	private int selectedHotbarSlot;

	private int totalEXP;
	private float xpProgress;
	private int level;

	private int hunger;
	private float saturation;

	private ServerConnection connection;

	public ThePlayer(ServerConnection connection) {
		super(0, connection.getPlayerUUID(), new DirectedLocation(), new Velocity());
		this.openInventories = new TreeMap<Byte, Inventory>();
		this.openInventories.put((byte) 0, new PlayerInventory());
		this.connection = connection;
	}

	public void updateXP(float progress, int level, int totalXP) {
		connection.getEventHandler().broadcast(new XPChangeEvent(this.xpProgress, this.level, this.totalEXP, progress, level, totalXP));
		this.level = level;
		this.xpProgress = progress;
		this.totalEXP = totalXP;
	}
	
	public void updateHunger(int hunger, float saturation) {
		this.hunger = hunger;
		this.saturation = saturation;
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
	 * @return Player hunger
	 */
	public int getHunger() {
		return hunger;
	}

	public DirectedLocation getHeadLocation() {
		return new DirectedLocation(location.add(headOffSet), location.getYaw(), location.getPitch());
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
	public PlayerInventory getPlayerInventory() {
		return (PlayerInventory) openInventories.get((byte) 0);
	}

	public Inventory getInventory(byte id) {
		if (id == -1) {
			// -1 is used to access the cursor slot, so we can just use the
			// player inventory, which will always be
			// present
			id = 0;
		}
		return openInventories.get(id);
	}

	public void addInventory(Inventory inv, byte id) {
		openInventories.put(id, inv);
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

	public void removeOpenInventory(byte id) {
		// always keep player inventory
		if (id != 0) {
			openInventories.remove(id);
		}
	}

	/**
	 * @return Players hunger saturation
	 */
	public float getSaturation() {
		return saturation;
	}

	public void setPlayerEntityID(int id) {
		this.id = id;
	}
}
