package com.maqs.moneytracker.common.service.exception;

import javax.xml.ws.WebFault;

/**
 * Base exception for all the runtime exceptions.
 * @author Maqbool.Ahmed
 *
 */
@WebFault(name = "ServiceFault")
public abstract class UncheckedServiceException extends RuntimeException {

	private static final long serialVersionUID = -7657111991371365664L;

	protected abstract String getErrorCode();
	
	/**
	 * Constructs the exception with the message.
	 * @param message exception message
	 */
	public UncheckedServiceException(String message) {
		super(message);
	}

	/**
	 * Constructs with the supplied message & the cause.
	 * @param message exception message
	 * @param cause Cause of the exception
	 */
	public UncheckedServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs with the cause.
	 * @param cause Cause of the exception
	 */
	public UncheckedServiceException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Gets the message for the corresponding errorCode.
	 */
	public String getMessage() {
		Object errorParam = super.getMessage();
		return ExceptionUtils.buildMessage(ServiceException.getMessagesBundle(), getErrorCode(), errorParam);
	}
	
}
