package com.github.maxopoly.angeliacore.exceptions;

/**
 * Thrown when data is attempted to be decompressed, but the data does not match
 * the attempted (de-)compression format
 *
 */
public class MalformedCompressedDataException extends Exception {
	
	public MalformedCompressedDataException(String msg) {
		super(msg);
	}
}
