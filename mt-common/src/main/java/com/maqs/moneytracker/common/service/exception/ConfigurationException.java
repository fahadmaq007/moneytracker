package com.maqs.moneytracker.common.service.exception;

/**
 * Denotes that the specified resource is not available.
 * @author Maqbool.Ahmed
 *
 */
public class ConfigurationException extends UncheckedServiceException {

	private static final long serialVersionUID = -7657111991342365664L;

	private static final String ERROR_CODE = "error.code.cfgexception";
	
	/**
	 * Constructs the exception with the message.
	 * @param message exception message
	 */
	public ConfigurationException(String message) {
		super(message);
	}

	/**
	 * Constructs with the supplied message & the cause.
	 * @param message exception message
	 * @param cause Cause of the exception
	 */
	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs with the supplied cause.
	 * @param cause Cause of the exception
	 */
	public ConfigurationException(Throwable cause) {
		super(cause);
	}
	
	@Override
	protected String getErrorCode() {
		return ERROR_CODE;
	}

}

