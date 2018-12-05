package com.github.maxopoly.angeliacore.plugin.parameter;

public class InvalidParameterValueException extends RuntimeException {

	private static final long serialVersionUID = -6603220618719580965L;
	
	public InvalidParameterValueException() {
		super();
	}
	
	public InvalidParameterValueException(String msg) {
		super(msg);
	}

}
