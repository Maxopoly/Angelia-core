package com.github.maxopoly.angeliacore.model.block.states;

public enum PowerableStateEnum {
	NOT_POWERED, POWERED;

	public boolean isPowered() {
		return this == POWERED;
	}

}
