package com.maqs.moneytracker.common.service.exception;


/**
 * Base exception for all the exceptions.
 * @author Maqbool.Ahmed
 *
 */
public abstract class NestedServiceException extends GenericException {

	/** Use serialVersionUID from Spring 1.2 for interoperability */
	private static final long serialVersionUID = 7100714597678207546L;


	/**
	 * Construct a <code>NestedCheckedException</code> with the specified detail message.
	 * @param msg the detail message
	 */
	public NestedServiceException(String msg) {
		this(null, null);
	}

	/**
	 * Construct a <code>NestedCheckedException</code> with the specified detail message
	 * and nested exception.
	 * @param msg the detail message
	 * @param cause the nested exception
	 */
	public NestedServiceException(String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs with the supplied message & the cause.
	 * @param cause Cause of the exception
	 */
	public NestedServiceException(Throwable cause) {
		super(cause);
	}
	
	/**
	 * Return the detail message, including the message from the nested exception
	 * if there is one.
	 */
	public String getMessage() {
		StringBuilder message = new StringBuilder();
		Throwable c = null; 
		Throwable cause = null;
		Throwable rootCause = null;
		
		while (c != null && c != rootCause) {
			cause = c;
			rootCause = cause;
			String msg = cause.getMessage();
			message.append(msg);
			c = cause.getCause();
		}
		return message.toString();
	}


	/**
	 * Retrieve the innermost cause of this exception, if any.
	 * @return the innermost exception, or <code>null</code> if none
	 */
	public Throwable getRootCause() {
		Throwable rootCause = null;
		Throwable cause = getCause();
		while (cause != null && cause != rootCause) {
			rootCause = cause;
			cause = cause.getCause();
		}
		return rootCause;
	}

	/**
	 * Retrieve the most specific cause of this exception, that is,
	 * either the innermost cause (root cause) or this exception itself.
	 * <p>Differs from {@link #getRootCause()} in that it falls back
	 * to the present exception if there is no root cause.
	 * @return the most specific cause (never <code>null</code>)
	 * @since 2.0.3
	 */
	public Throwable getMostSpecificCause() {
		Throwable rootCause = getRootCause();
		return (rootCause != null ? rootCause : this);
	}

	/**
	 * Check whether this exception contains an exception of the given type:
	 * either it is of the given class itself or it contains a nested cause
	 * of the given type.
	 * @param exType the exception type to look for
	 * @return whether there is a nested exception of the specified type
	 */
	public boolean contains(Class exType) {
		if (exType == null) {
			return false;
		}
		if (exType.isInstance(this)) {
			return true;
		}
		Throwable cause = getCause();
		if (cause == this) {
			return false;
		}
		if (cause instanceof NestedServiceException) {
			return ((NestedServiceException) cause).contains(exType);
		}
		else {
			while (cause != null) {
				if (exType.isInstance(cause)) {
					return true;
				}
				if (cause.getCause() == cause) {
					break;
				}
				cause = cause.getCause();
			}
			return false;
		}
	}
}
