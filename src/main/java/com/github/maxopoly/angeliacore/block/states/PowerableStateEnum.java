package com.github.maxopoly.angeliacore.block.states;

public enum PowerableStateEnum {
	NOT_POWERED, POWERED;
	
	public boolean isPowered() {
		return this == POWERED;
	}
	
}
