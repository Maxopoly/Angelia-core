package com.github.maxopoly.angeliacore.block;

import com.github.maxopoly.angeliacore.block.states.BlockState;
import com.github.maxopoly.angeliacore.block.states.StaticBlockState;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class BlockStateFactory {

	private static Map <Integer, BlockState> cachedStates = new ConcurrentHashMap<Integer, BlockState>();

	public static BlockState getState(int data) {
		return getCachedState(data);
	}

	private static BlockState getCachedState(int data) {
		BlockState state = cachedStates.get(data);
		if (state == null) {
			byte metadata = (byte) (data & 0xF);
		    int id = data >> 4;
			state = new StaticBlockState(id, metadata);
			cachedStates.put(data, state);
		}
		return state;
	}

}
