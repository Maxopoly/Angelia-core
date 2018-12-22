package com.github.maxopoly.angeliacore.connection.login;

/**
 * Thrown when auth is attempted to be updated, but the auth server returns 403,
 * indicating that this account or the entire client is rate limited
 *
 */
public class Auth403Exception extends Exception {

	private static final long serialVersionUID = -6284844258918050318L;

	public Auth403Exception() {
		super();
	}

	public Auth403Exception(String msg) {
		super(msg);
	}

}
