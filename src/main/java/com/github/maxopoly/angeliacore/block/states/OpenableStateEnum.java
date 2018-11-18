package com.github.maxopoly.angeliacore.block.states;

public enum OpenableStateEnum {
	
	CLOSED, OPEN;
	
	public boolean isOpen() {
		return this == OPEN;
	}

}
