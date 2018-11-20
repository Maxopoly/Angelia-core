package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class BrewingStandInventory extends Inventory {

	private short brewingTime;
	private short fuelTime;

	public BrewingStandInventory(byte windowID) {
		super(41, windowID);
	}

	public ItemStack getFuelSlot() {
		return slots[4];
	}

	public ItemStack getIngredientSlot() {
		return slots[3];
	}

	public ItemStack getLeftResultSlot() {
		return slots[0];
	}

	public ItemStack getMiddleResultSlot() {
		return slots[1];
	}

	public ItemStack getRightResultSlot() {
		return slots[2];
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 5;
	}

	public void setBrewingTime(short brewTime) {
		this.brewingTime = brewTime;
	}

	public void setFuelTime(short fuelTime) {
		this.fuelTime = fuelTime;
	}

	/**
	 * @return How far the brewing process is done in the range [0,400] (for vanilla
	 *         servers)
	 */
	public short getBrewingTime() {
		return brewingTime;
	}

	/**
	 * @return How far we are into fuel consumption in the range [0,20] (for vanilla
	 */
	public short getFuelTime() {
		//TODO fuel and brewing time may be swapped around, current docu is copied from the protocol wiki
		return fuelTime;
	}

}