package com.github.maxopoly.angeliacore.model.block;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.github.maxopoly.angeliacore.model.block.states.ActivatableFacingBlock;
import com.github.maxopoly.angeliacore.model.block.states.AirBlockState;
import com.github.maxopoly.angeliacore.model.block.states.BlockState;
import com.github.maxopoly.angeliacore.model.block.states.FancySpecialBlockState;
import com.github.maxopoly.angeliacore.model.block.states.SolidStaticBlockState;
import com.github.maxopoly.angeliacore.model.block.states.VariableBlockState;
import com.github.maxopoly.angeliacore.model.block.states.rail.NormalRailBlockState;
import com.github.maxopoly.angeliacore.model.block.states.rail.SpecialRailBlockState;

public class BlockStateFactory {

	private static Map<Integer, BlockState> blockStatesById = new ConcurrentHashMap<>();
	private static Map<String, BlockState> blockStatesByTextureName = new ConcurrentHashMap<>();

	private static BlockState placeHolder;

	static {
		initialize();
	}

	public static BlockState getStateByData(int data) {
		BlockState state = blockStatesById.get(data);
		if (state == null) {
			return placeHolder;
		}
		return state.getActualState((byte) data);
	}

	public static BlockState getStateByTextureIdentifier(String texture) {
		return blockStatesByTextureName.get(texture);
	}

	private static void initialize() {
		register(new AirBlockState());
		SolidStaticBlockState stone = new SolidStaticBlockState(1, 1.5f, "stone", "Stone");
		placeHolder = stone;
		register(stone);
		register(new SolidStaticBlockState(1, (byte) 1, 1.5f, "granite", "Granite"));
		register(new SolidStaticBlockState(1, (byte) 2, 1.5f, "granite_smooth", "Polished Granite"));
		register(new SolidStaticBlockState(1, (byte) 3, 1.5f, "diorite", "Granite"));
		register(new SolidStaticBlockState(1, (byte) 4, 1.5f, "diorite_smooth", "Polished Granite"));
		register(new SolidStaticBlockState(1, (byte) 5, 1.5f, "andesite", "Granite"));
		register(new SolidStaticBlockState(1, (byte) 6, 1.5f, "andesite_smooth", "Polished Granite"));
		register(new SolidStaticBlockState(2, 0.6f, "grass", "Grass Block"));
		register(new SolidStaticBlockState(3, 0.5f, "dirt", "Dirt"));
		register(new SolidStaticBlockState(3, (byte) 1, 0.5f, "coarse_dirt", "Coarse Dirt"));
		register(new SolidStaticBlockState(3, (byte) 2, 0.5f, "podzol", "Podzol"));
		register(new SolidStaticBlockState(4, 2.0f, "cobblestone", "Cobblestone"));
		registerWoodVariations(5, 2.0f, "_planks", " Plank");
		register(new FancySpecialBlockState(6, 0f, "oak_sapling", "Oak Sapling"));
		register(new FancySpecialBlockState(6, (byte) 1, 0f, "spruce_sapling", "Spruce Sapling"));
		register(new FancySpecialBlockState(6, (byte) 2, 0f, "birch_sapling", "Birch Sapling"));
		register(new FancySpecialBlockState(6, (byte) 3, 0f, "jungle_sapling", "Jungle Sapling"));
		register(new FancySpecialBlockState(6, (byte) 4, 0f, "acacia_sapling", "Acacia Sapling"));
		register(new FancySpecialBlockState(6, (byte) 5, 0f, "dark_oak_sapling", "Dark Oak Sapling"));
		register(new SolidStaticBlockState(7, 18000000f, "bedrock", "Bedrock"));

		register(new SolidStaticBlockState(12, 0.5f, "sand", "Sand"));
		register(new SolidStaticBlockState(12, (byte) 1, 0.5f, "red_sand", "Red Sand"));
		register(new SolidStaticBlockState(13, 0.6f, "gravel", "Gravel"));
		register(new SolidStaticBlockState(14, 3f, "gold_ore", "Gold Ore"));
		register(new SolidStaticBlockState(15, 3f, "iron_ore", "Iron Ore"));

		register(new SolidStaticBlockState(22, 3f, "lapis_block", "Lapis Lazuli Block"));
		multiRegister(new VariableBlockState(ActivatableFacingBlock.class, 23, 3.5f, "dispenser", "Dispenser"));
		register(new SolidStaticBlockState(24, 0.8f, "sandstone", "Sandstone"));
		register(new SolidStaticBlockState(24, (byte) 1, 0.8f, "chiseled_sandstone", "Chiseled Sandstone"));
		register(new SolidStaticBlockState(24, (byte) 2, 0.8f, "smooth_sandstone", "Smooth Sandstone"));
		register(new SolidStaticBlockState(25, 0.8f, "noteblock", "Note Block"));
		// TODO Bed does not have a block state in texure packs
		multiRegister(new VariableBlockState(SpecialRailBlockState.class, 27, 0.7f, "golden_rail", "Powered Rail"));
		multiRegister(new VariableBlockState(SpecialRailBlockState.class, 28, 0.7f, "detector_rail", "Detector Rail"));

		multiRegister(new VariableBlockState(NormalRailBlockState.class, 66, 0.7f, "rail", "Rail"));

		registerWoodVariations(125, 2.0f, "_double_slab", " Double Slab");
		registerWoodVariations(126, 2.0f, "_slab", " Slab");

		register(new SolidStaticBlockState(133, 5f, "emerald_block", "Emerald Block"));

		multiRegister(
				new VariableBlockState(SpecialRailBlockState.class, 157, 0.7f, "activator_rail", "Activator Rail"));
		multiRegister(new VariableBlockState(ActivatableFacingBlock.class, 158, 3.5f, "dropper", "Dropper"));
	}

	private static void internalRegister(int id, byte metaData, BlockState state) {
		int data = id << 4;
		data |= metaData & 0xf;
		blockStatesById.put(data, state);
		blockStatesByTextureName.put(state.getTexturePackIdentifier(), state);
	}

	private static void multiRegister(BlockState state) {
		for (byte i = 0; i < 16; i++) {
			internalRegister(state.getID(), i, state);
		}
	}

	private static void register(BlockState state) {
		internalRegister(state.getID(), state.getMetaData(), state);
	}

	private static void registerWoodVariations(int id, float hardness, String texPackSuffix, String niceNameSuffix) {
		register(new SolidStaticBlockState(id, hardness, "oak" + texPackSuffix, "Oak" + niceNameSuffix));
		register(
				new SolidStaticBlockState(id, (byte) 1, hardness, "spruce" + texPackSuffix, "Spruce" + niceNameSuffix));
		register(new SolidStaticBlockState(id, (byte) 2, hardness, "birch" + texPackSuffix, "Birch" + niceNameSuffix));
		register(
				new SolidStaticBlockState(id, (byte) 3, hardness, "jungle" + texPackSuffix, "Jungle" + niceNameSuffix));
		register(
				new SolidStaticBlockState(id, (byte) 4, hardness, "acacia" + texPackSuffix, "Acacia" + niceNameSuffix));
		register(new SolidStaticBlockState(id, (byte) 5, hardness, "dark_oak" + texPackSuffix,
				"Dark Oak" + niceNameSuffix));
	}

}
