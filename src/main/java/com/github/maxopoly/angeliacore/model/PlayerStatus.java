package com.github.maxopoly.angeliacore.model;

import com.github.maxopoly.angeliacore.connection.ServerConnection;
import com.github.maxopoly.angeliacore.event.events.HealthChangeEvent;
import com.github.maxopoly.angeliacore.event.events.HungerChangeEvent;
import com.github.maxopoly.angeliacore.event.events.XPChangeEvent;
import com.github.maxopoly.angeliacore.model.entity.LivingEntity;
import com.github.maxopoly.angeliacore.model.inventory.Inventory;
import com.github.maxopoly.angeliacore.model.inventory.PlayerInventory;
import com.github.maxopoly.angeliacore.model.location.Location;
import com.github.maxopoly.angeliacore.model.player.OnlinePlayer;
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

public class PlayerStatus {

	public static final double headDelta = 1.62; // how far the head is above the player location

	private Map<Byte, Inventory> openInventories = new TreeMap<Byte, Inventory>(){{
	    put((byte)0, new PlayerInventory());
    }};
	private Map<PotionEffect, Long> potionEffects = new HashMap<>();

	private LivingEntity entity;

	private int selectedHotbarSlot;

	private int totalEXP;
	private float xpProgress;
	private int level;

	private float health = 20.f;
	private int hunger;
	private float saturation;
	private boolean midAir = false;

	private DecimalFormat format = new DecimalFormat("#.##");

	private ServerConnection connection;

	public PlayerStatus(ServerConnection connection) {
		this.connection = connection;
	}

	public void updateXP(float progress, int level, int totalXP) {
		connection.getEventHandler().broadcast(new XPChangeEvent(this.xpProgress, this.level, this.totalEXP, progress, level, totalXP));
		this.level = level;
		this.xpProgress = progress;
		this.totalEXP = totalXP;
	}

	public void updateLocation(Location location) {
	    entity.updateLocation(location);
	}

	public void updatePosition(double x, double y, double z) {
	    entity.updatePosition(x, y, z);
	}

	public void updateLookingDirection(float yaw, float pitch) {
	    entity.updateLookingDirection(yaw, pitch);
	}

	public void updateHealth(float health, int hunger, float saturation) {
		if (this.health != health) {
			connection.getEventHandler().broadcast(
					new HealthChangeEvent(this.health, health));
		}
		this.health = health;
		if (this.hunger != hunger) {
			connection.getEventHandler().broadcast(
					new HungerChangeEvent(this.hunger, hunger));
		}
		this.hunger = hunger;
		this.saturation = saturation;
	}

	/**
	 * @return Player location
	 */
	public Location getLocation() {
	    return isInitialized() ? entity.getLocation() : new Location(0, 0, 0);
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
	    Location location = entity.getLocation();
		return new Location(location.getX(), location.getY() + headDelta,
				location.getZ(), location.getYaw(), location.getPitch());
	}

	/**
	 * @return Whether the location was initialized yet
	 */
	public boolean isInitialized() {
		return entity != null;
	}

	/**
	 * @return Whether the player is falling/flying
	 */
	public boolean isMidAir() {
		return midAir;
	}

	/**
	 * Sets the flying/falling state
	 *
	 * @param air
	 *            Whether the player is not on the ground
	 */
	public void setMidAir(boolean air) {
		this.midAir = air;
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

	public List<PotionEffect> getActivePotionEffects() {
		filterTimedOutEffects();
		synchronized (potionEffects) {
			return new LinkedList<PotionEffect>(potionEffects.keySet());
		}
	}

	public void addPotionEffect(PotionEffect effect) {
		synchronized (potionEffects) {
			potionEffects.remove(effect);
			potionEffects.put(effect, System.currentTimeMillis());
		}
	}

	public void removeOpenInventory(byte id) {
		// always keep player inventory
		if (id != 0) {
			openInventories.remove(id);
		}
	}

	private void filterTimedOutEffects() {
		synchronized (potionEffects) {
			Set<Entry<PotionEffect, Long>> entrySet = potionEffects.entrySet();
			Iterator<Entry<PotionEffect, Long>> iter = entrySet.iterator();
			while (iter.hasNext()) {
				Entry<PotionEffect, Long> entry = iter.next();
				long start = entry.getValue();
				long runningTime = entry.getKey().getDuration() * 1000;
				long current = System.currentTimeMillis();
				if ((current - start) > runningTime) {
					iter.remove();
				}
			}
		}
	}

	/**
	 * @return Players hunger saturation
	 */
	public float getSaturation() {
		return saturation;
	}

	public String getLocationString() {
		return entity.getLocation().toString();
	}

	/**
	 * @return The players entity id
	 */
	public LivingEntity getEntity() {
		return entity;
	}

	public void setEntity(LivingEntity entity) {
		this.entity = entity;
	}

	public String getHealthString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Health: " + format.format(health));
		sb.append(", Hunger: " + hunger);
		sb.append(", Saturation: " + format.format(saturation));
		return sb.toString();
	}

	public String getXPString() {
		return "Level: " + level + ", totalXP: " + totalEXP + ", progress: "
				+ format.format(xpProgress);
	}

	@Override
	public String toString() {
		return getLocationString() + " ; " + getHealthString() + " ; "
				+ getXPString();
	}
}
