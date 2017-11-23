package com.maqs.moneytracker.common.service.runtimeexception;

/**
 * Denotes that the specified resource is not available.
 * @author Maqbool.Ahmed
 *
 */
public class MissingResourceException extends RuntimeException {

	private static final long serialVersionUID = -7643211991342365664L;

	/**
	 * Constructs the exception with the message.
	 * @param message exception message
	 */
	public MissingResourceException(String message) {
		super(message);
	}

	/**
	 * Constructs with the supplied message & the cause.
	 * @param message exception message
	 * @param cause Cause of the exception
	 */
	public MissingResourceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs with the supplied cause.
	 * @param cause Cause of the exception
	 */
	public MissingResourceException(Throwable cause) {
		super(cause);
	}
	
}

