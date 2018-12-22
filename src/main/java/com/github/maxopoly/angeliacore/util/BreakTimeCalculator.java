package com.github.maxopoly.angeliacore.util;

import java.util.List;

import com.github.maxopoly.angeliacore.model.item.Enchantment;
import com.github.maxopoly.angeliacore.model.item.ItemStack;
import com.github.maxopoly.angeliacore.model.item.ToolType;
import com.github.maxopoly.angeliacore.model.potion.PotionEffect;
import com.github.maxopoly.angeliacore.model.potion.PotionType;

public class BreakTimeCalculator {

	public static int getBreakTicks(double baseHardness, boolean acceptableTool, boolean rightTool, ItemStack tool,
			List<PotionEffect> activeBuffs, double ticksPerSecond) {
		double totalHardness = baseHardness * 30;
		@SuppressWarnings("unused")
		ToolType toolType;
		int multiplier;
		if (!rightTool || tool == null || tool.isEmpty()) {
			toolType = ToolType.OTHER;
			multiplier = 1;
		} else {
			switch (tool.getMaterial()) {
			case DIAMOND_AXE:
				toolType = ToolType.AXE;
				multiplier = 8;
				break;
			case DIAMOND_PICKAXE:
				toolType = ToolType.PICKAXE;
				multiplier = 8;
				break;
			case DIAMOND_SPADE:
				toolType = ToolType.SHOVEL;
				multiplier = 8;
				break;
			case DIAMOND_SWORD:
				toolType = ToolType.SWORD;
				multiplier = 8;
				break;
			case GOLD_AXE:
				toolType = ToolType.AXE;
				multiplier = 12;
				break;
			case GOLD_PICKAXE:
				toolType = ToolType.PICKAXE;
				multiplier = 12;
				break;
			case GOLD_SPADE:
				toolType = ToolType.SHOVEL;
				multiplier = 12;
				break;
			case GOLD_SWORD:
				toolType = ToolType.SWORD;
				multiplier = 12;
				break;
			case IRON_AXE:
				toolType = ToolType.AXE;
				multiplier = 6;
				break;
			case IRON_PICKAXE:
				toolType = ToolType.PICKAXE;
				multiplier = 6;
				break;
			case IRON_SPADE:
				toolType = ToolType.SHOVEL;
				multiplier = 6;
				break;
			case IRON_SWORD:
				toolType = ToolType.SWORD;
				multiplier = 6;
				break;
			case STONE_AXE:
				toolType = ToolType.AXE;
				multiplier = 4;
				break;
			case STONE_PICKAXE:
				toolType = ToolType.PICKAXE;
				multiplier = 4;
				break;
			case STONE_SPADE:
				toolType = ToolType.SHOVEL;
				multiplier = 4;
				break;
			case STONE_SWORD:
				toolType = ToolType.SWORD;
				multiplier = 4;
				break;
			case WOOD_AXE:
				toolType = ToolType.AXE;
				multiplier = 2;
				break;
			case WOOD_PICKAXE:
				toolType = ToolType.PICKAXE;
				multiplier = 2;
				break;
			case WOOD_SPADE:
				toolType = ToolType.SHOVEL;
				multiplier = 2;
				break;
			case WOOD_SWORD:
				toolType = ToolType.SWORD;
				multiplier = 2;
				break;
			case SHEARS:
				toolType = ToolType.SHEARS;
				multiplier = 15;
				break;
			default:
				toolType = ToolType.OTHER;
				multiplier = 1;
			}
		}
		int efficiencyBonus = 0;
		if (rightTool && tool.isEnchanted()) {
			Integer level = tool.getEnchants().get(Enchantment.EFFICIENCY);
			if (level != null) {
				efficiencyBonus = (level * level) + 1;
			}
		}
		double effectMultiplier = 1.0;
		for (PotionEffect effect : activeBuffs) {
			if (effect.getType() == PotionType.HASTE) {
				effectMultiplier *= (1.0 + (0.2 * effect.getStrength()));
			} else if (effect.getType() == PotionType.MINING_FATIGUE) {
				effectMultiplier *= Math.pow(0.3, effect.getStrength());
			}
		}
		if (!acceptableTool) {
			return (int) Math.ceil(5.0 * baseHardness / effectMultiplier * ticksPerSecond);
		}
		double damagePerTick = (multiplier + efficiencyBonus) * effectMultiplier;
		if (damagePerTick > totalHardness) {
			// instabreak
			return 0;
		}
		return (int) Math.ceil(totalHardness / damagePerTick);
	}
}
