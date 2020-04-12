package com.github.maxopoly.angeliacore.model.block.states;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class VariableBlockState extends BlockState {

	private Map<Byte, BlockState> stateMapping;
	private Class<? extends BlockState> instanceClass;

	public VariableBlockState(Class<? extends BlockState> instanceClass, int id, float hardness,
			String texturePackIdentifier, String niceName, boolean hasCollision) {
		super(id, (byte) 0, hardness, texturePackIdentifier, niceName, hasCollision);
		this.instanceClass = instanceClass;
		stateMapping = new WeakHashMap<>();
	}

	@Override
	public BlockState getActualState(byte data) {
		return getState((byte) (data & 0xf));
	}

	@Override
	public double getHardness() {
		throw new IllegalStateException();
	}

	@Override
	public byte getMetaData() {
		throw new IllegalStateException();
	}

	@Override
	public int getMetaData(@SuppressWarnings("rawtypes") List<Enum> enums) {
		throw new IllegalStateException();
	}

	private BlockState getState(byte data) {
		BlockState state = stateMapping.get(data);
		if (state == null) {
			try {
				state = (BlockState) instanceClass.getConstructors()[0].newInstance(id, data, hardness,
						texturePackIdentifier, niceName);
				state.setRenderModule(this.render);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | SecurityException e) {
				e.printStackTrace();
				return null;
			}
			stateMapping.put(data, state);
		}
		return state;
	}

	@Override
	public boolean isFullBlock() {
		throw new IllegalStateException();
	}

	@Override
	public boolean isLiquid() {
		throw new IllegalStateException();
	}

	@Override
	public boolean isOpaque() {
		throw new IllegalStateException();
	}

}
