package com.github.maxopoly.angeliacore.block;

import com.github.maxopoly.angeliacore.block.states.AirBlockState;
import com.github.maxopoly.angeliacore.block.states.BlockState;
import com.github.maxopoly.angeliacore.block.states.FancySpecialBlockState;
import com.github.maxopoly.angeliacore.block.states.SolidStaticBlockState;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockStateFactory {

	private static Map<Integer, BlockState> blockStatesById = new ConcurrentHashMap<>();
	private static Map<String, BlockState> blockStatesByTextureName = new ConcurrentHashMap<>();

	private static BlockState placeHolder = new SolidStaticBlockState(1, 1.5f, "stone", "Stone");

	static {
		initialize();
	}

	public static BlockState getStateByData(int data) {
		BlockState state = blockStatesById.get(data);
		if (state == null) {
			return placeHolder;
		}
		return state;
	}

	public BlockState getStateByTextureIdentifier(String texture) {
		return blockStatesByTextureName.get(texture);
	}

	private static void register(BlockState state) {
		int data = state.getID() << 4;
		data |= state.getMetaData() & 0xf;
		blockStatesById.put(data, state);
		blockStatesByTextureName.put(state.getTexturePackIdentifier(), state);
	}

	private static void initialize() {
		register(new AirBlockState());
		register(new SolidStaticBlockState(1, 1.5f, "stone", "Stone"));
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
		register(new SolidStaticBlockState(5, 2.0f, "oak_planks", "Oak Plank"));
		register(new SolidStaticBlockState(5, (byte) 1, 2.0f, "spruce_planks", "Spruce Plank"));
		register(new SolidStaticBlockState(5, (byte) 2, 2.0f, "birch_planks", "Birch Plank"));
		register(new SolidStaticBlockState(5, (byte) 3, 2.0f, "jungle_planks", "Jungle Plank"));
		register(new SolidStaticBlockState(5, (byte) 4, 2.0f, "acacia_planks", "Acacia Plank"));
		register(new SolidStaticBlockState(5, (byte) 5, 2.0f, "dark_oak_planks", "Dark Oak Plank"));
		register(new FancySpecialBlockState(6, 0f, "oak_sapling", "Oak Sapling"));
		register(new FancySpecialBlockState(6, (byte) 1, 0f, "spruce_sapling", "Spruce Sapling"));
		register(new FancySpecialBlockState(6, (byte) 2, 0f, "birch_sapling", "Birch Sapling"));
		register(new FancySpecialBlockState(6, (byte) 3, 0f, "jungle_sapling", "Jungle Sapling"));
		register(new FancySpecialBlockState(6, (byte) 4, 0f, "acacia_sapling", "Acacia Sapling"));
		register(new FancySpecialBlockState(6, (byte) 5, 0f, "dark_oak_sapling", "Dark Oak Sapling"));
		register(new SolidStaticBlockState(7, 18000000f, "bedrock", "Bedrock"));
	}

}
