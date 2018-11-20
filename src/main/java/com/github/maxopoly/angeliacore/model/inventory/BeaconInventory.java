package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.model.potion.PotionType;

public class BeaconInventory extends Inventory {

	private short powerLevel;
	private PotionType primaryEffect;
	private PotionType secondaryEffect;

	public BeaconInventory(byte windowID) {
		super(37, windowID);
	}

	public ItemStack getPaymentSlot() {
		return slots[0];
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 1;
	}

	/**
	 * @return Controls which buttons are enabled in vanilla clients. Always within
	 *         [0,4] for vanilla servers
	 */
	public short getPowerLevel() {
		return powerLevel;
	}
	
	public void setPowerLevel(short powerLevel) {
		this.powerLevel = powerLevel;
	}
	
	public void setPrimaryEffect(PotionType primary) {
		this.primaryEffect = primary;
	}
	
	public void setSecondaryEffect(PotionType secondary) {
		this.secondaryEffect = secondary;
	}
	
	/**
	 * @return Primary selected effect of the beacon, may be null
	 */
	public PotionType getPrimaryEffect() {
		return primaryEffect;
	}
	
	/**
	 * @return Secondary selected effect of the beacon, may be null
	 */
	public PotionType getSecondaryEffect() {
		return secondaryEffect;
	}
}