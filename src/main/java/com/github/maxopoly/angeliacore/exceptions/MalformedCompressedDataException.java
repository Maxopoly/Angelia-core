package com.github.maxopoly.angeliacore.exceptions;

/**
 * Thrown when data is attempted to be decompressed, but the data does not match
 * the attempted (de-)compression format
 *
 */
public class MalformedCompressedDataException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2490078493605285149L;
	
	public MalformedCompressedDataException() {
		super();
	}

	public MalformedCompressedDataException(String msg) {
		super(msg);
	}
}
