package com.github.maxopoly.angeliacore.exceptions;

/**
 * Thrown by methods or the constructor of AuthenticationHandler representing invalid and unrefreshable auth data
 */
public class OutDatedAuthException extends Exception {

	public OutDatedAuthException(String msg) {
		super(msg);
	}

}
