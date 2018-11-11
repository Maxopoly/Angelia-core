package com.github.maxopoly.angeliacore.exceptions;

/**
 * Thrown by methods or the constructor of AuthenticationHandler representing invalid and unrefreshable auth data
 */
public class OutDatedAuthException extends Exception {

	private static final long serialVersionUID = 4837340943866774529L;
	
	public OutDatedAuthException() {
		super();
	}

	public OutDatedAuthException(String msg) {
		super(msg);
	}

}
