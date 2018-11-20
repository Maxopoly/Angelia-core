package com.github.maxopoly.angeliacore.model.inventory;

import com.github.maxopoly.angeliacore.model.item.Enchantment;
import com.github.maxopoly.angeliacore.model.item.ItemStack;

public class EnchantmentTableInventory extends Inventory {
	
	public enum EnchantingSlot {
		TOP, MIDDLE, BOTTOM;
		
		public static EnchantingSlot parse(int index) {
			switch(index) {
			case 0:
			case 4:
			case 7:
				return TOP;
			case 1:
			case 5:
			case 8:
				return MIDDLE;
			case 2:
			case 6:
			case 9:
				return BOTTOM;
			}
			throw new IllegalArgumentException(index + " is not a valid enchantment slot index");
		}
		
	}
	
	private Enchantment [] availablePreviewEnchants;
	private Short [] availablePreviewEnchantLevels;
	private Short [] availableEnchantLevelCosts;
	private short enchantingSeed;

	public EnchantmentTableInventory(byte windowID) {
		super(38, windowID);
		availablePreviewEnchants = new Enchantment [3];
		availablePreviewEnchantLevels = new Short [3];
		availableEnchantLevelCosts = new Short [3];
	}
	
	/**
	 * Seed used by vanilla clients for the randomized enchanting animation
	 */
	public short getEnchantingSeed() {
		return enchantingSeed;
	}
	
	public void setEnchantingSeed(short seed) {
		this.enchantingSeed = seed;
	}
	
	public void setPreviewEnchant(EnchantingSlot slot, Enchantment enchant) {
		availablePreviewEnchants [slot.ordinal()] = enchant;
	}
	
	/**
	 * @return The enchantment shown in the preview when hovering an enchantment slot
	 */
	public Enchantment getEnchantmentPrewview(EnchantingSlot slot) {
		return availablePreviewEnchants [slot.ordinal()];
	}
	
	public void setPreviewEnchantLevel(EnchantingSlot slot, short level) {
		availablePreviewEnchantLevels [slot.ordinal()] = level;
	}
	
	/**
	 * @return The level of the enchantment shown as preview on the slot
	 */
	public short getPreviewEnchantLevel(EnchantingSlot slot) {
		Short value = availablePreviewEnchantLevels [slot.ordinal()];
		if (value == null) {
			value = -1;
		}
		return value;
	}
	
	public void setEnchantingCost(EnchantingSlot slot, short cost) {
		availableEnchantLevelCosts [slot.ordinal()] = cost;
	}
	
	/**
	 * @return Cost in levels for this enchantment
	 */
	public short getEnchantingCost(EnchantingSlot slot) {
		Short value = availableEnchantLevelCosts [slot.ordinal()];
		if (value == null) {
			value = -1;
		}
		return value;
	}
	
	public ItemStack getEnchantingSlot() {
		return slots [0];
	}
	
	public ItemStack getLapisSlot() {
		return slots [1];
	}

	@Override
	protected int getPlayerStorageStartingSlot() {
		return 2;
	}

}
