package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class FurnaceInventory extends Inventory {

	private short fuelLeft;
	private short maximumFuelBurnTime;
	private short smeltingProgress;
	private short maximumProgress;

	public FurnaceInventory(byte windowID) {
		super(39, windowID);
	}

	public ItemStack getFuelSlot() {
		return slots[1];
	}

	/**
	 * @return Serverside ticks left until the fuel/fire power currently available
	 *         has been completly used. This corresponds to the flame icon in normal
	 *         furnace GUIs
	 */
	public short getFuelTicksLeft() {
		return fuelLeft;
	}

	public ItemStack getIngredientSlot() {
		return slots[0];
	}

	/**
	 * @return Total fuel burn time of the fuel item last consumed
	 */
	public short getMaximumFuelBurnTime() {
		return maximumFuelBurnTime;
	}

	/**
	 * @return Total amount of server side ticks needed to complete one smelting
	 *         process. Always 200 in vanilla servers
	 */
	public short getMaximumSmeltingProgress() {
		return maximumProgress;
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 3;
	}

	public ItemStack getResultSlot() {
		return slots[2];
	}

	/**
	 * @return How far the smelting process has progressed on a scale from 0 to the
	 *         maximum smelting progress in server side ticks. Corresponds to the
	 *         arrow in normal furnace GUIs
	 */
	public short getSmeltingProgress() {
		return smeltingProgress;
	}

	public void setFuelTicksLeft(short fuelTicksLeft) {
		this.fuelLeft = fuelTicksLeft;
	}

	public void setMaximumFuelBurnTime(short maximumFuelBurnTime) {
		this.maximumFuelBurnTime = maximumFuelBurnTime;
	}

	public void setMaximumSmeltingProgress(short maxProgress) {
		this.maximumProgress = maxProgress;
	}

	public void setSmeltingProgress(short progress) {
		this.smeltingProgress = progress;
	}

}
