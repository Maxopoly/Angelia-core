package com.github.maxopoly.angeliacore.block;

/**
 * Thrown by the chunk holder when holding block data is disabled through the
 * config, but accessing it is attempted anyway
 *
 */
public class BlockModelNotHeldException extends RuntimeException {

	private static final long serialVersionUID = -1317025547139934692L;

	public BlockModelNotHeldException() {
		super();
	}

	public BlockModelNotHeldException(String msg) {
		super(msg);
	}

}
