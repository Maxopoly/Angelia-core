package com.github.maxopoly.angeliacore.plugin.parameter;

public enum LoadPolicy {

	CONFIG_ONLY, INPUT_ONLY, PRIORIZE_CONFIG, PRIORIZE_INPUT;

	public <T> T parse(T inputValue, T configValue) {
		switch (this) {
		case CONFIG_ONLY:
			return configValue;
		case INPUT_ONLY:
			return inputValue;
		case PRIORIZE_CONFIG:
			if (configValue != null) {
				return configValue;
			}
			return inputValue;
		case PRIORIZE_INPUT:
			if (inputValue != null) {
				return inputValue;
			}
			return configValue;
		default:
			throw new IllegalStateException();
		}
	}
}
