package com.github.maxopoly.angeliacore.model.player;

public class PlayerProperty {

	private String name;
	private String value;
	private boolean isSigned;
	private String signature;

	public PlayerProperty(String name, String value) {
		this(name, value, false, null);
	}

	public PlayerProperty(String name, String value, boolean isSigned, String signature) {
		this.name = name;
		this.value = value;
		this.isSigned = isSigned;
		this.signature = signature;
	}

	public String getName() {
		return name;
	}

	public String getSignature() {
		return signature;
	}

	public String getValue() {
		return value;
	}

	public boolean isSigned() {
		return isSigned;
	}

}
