package com.github.maxopoly.angeliacore.block;

import com.github.maxopoly.angeliacore.block.states.AirBlockState;
import com.github.maxopoly.angeliacore.block.states.BlockState;
import com.github.maxopoly.angeliacore.block.states.SolidStaticBlockState;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BlockStateFactory {

    private static Map<Integer, BlockState> blockStatesById = new ConcurrentHashMap<>();
    private static Map<String, BlockState> blockStatesByTextureName = new ConcurrentHashMap<>();

    static {
        initialize();
    }

    public static BlockState getStateByData(int data) {
        BlockState state = blockStatesById.get(data);
        if (state == null) {
            // todo return placeholder
        }
        return state;
    }

    public BlockState getStateByTextureIdentifier(String texture) {
        return blockStatesByTextureName.get(texture);
    }

    private static void register(BlockState state) {
        int data = state.getID() << 4;
        data &= state.getMetaData();
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
    }

}
