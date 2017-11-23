package com.maqs.moneytracker.common.service.runtimeexception;

/**
 * Denotes that the specified resource is not available.
 * @author Maqbool.Ahmed
 *
 */
public class SOAPRequestException extends RuntimeException {

	private static final long serialVersionUID = -7633334342365664L;

	/**
	 * Constructs the exception with the message.
	 * @param message exception message
	 */
	public SOAPRequestException(String message) {
		super(message);
	}

	/**
	 * Constructs with the supplied message & the cause.
	 * @param message exception message
	 * @param cause Cause of the exception
	 */
	public SOAPRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs with the supplied cause.
	 * @param cause Cause of the exception
	 */
	public SOAPRequestException(Throwable cause) {
		super(cause);
	}
	
}

