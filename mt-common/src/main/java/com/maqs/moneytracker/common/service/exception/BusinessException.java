package com.maqs.moneytracker.common.service.exception;

import javax.xml.ws.WebFault;

/**
 * BusinessException represents the business scenario that needs notification to the user.
 * 
 * @author Maqbool.Ahmed
 * 
 */
public class BusinessException extends ServiceException {

  private static final long serialVersionUID = -7657111991342365664L;

  /**
   * Constructs the exception with the message.
   * 
   * @param message exception message
   */
  public BusinessException(String errorCode, String message) {
    this(errorCode, message, null);
  }

  /**
   * Constructs with the supplied message & the cause.
   * 
   * @param message exception message
   * @param cause Cause of the exception
   */
  public BusinessException(String errorCode, String message, Throwable cause) {
    super(message, cause);
    setErrorCode(errorCode);
  }

  /**
   * Constructs with the cause.
   * 
   * @param cause Cause of the exception
   */
  public BusinessException(String errorCode, Throwable cause) {
    this(errorCode, cause.getMessage(), cause);
  }

}
