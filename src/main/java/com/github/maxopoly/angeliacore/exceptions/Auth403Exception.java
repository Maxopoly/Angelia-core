package com.github.maxopoly.angeliacore.exceptions;

/**
 * Thrown when auth is attempted to be updated, but the auth server returns 403, indicating that this account or the
 * entire client is rate limited
 *
 */
public class Auth403Exception extends Exception {

	public Auth403Exception(String msg) {
		super(msg);
	}

}
