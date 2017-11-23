package com.maqs.moneytracker.common.service.exception;


/**
 * Represents the service layer exception.
 * 
 * @author Maqbool.Ahmed
 * 
 */
public class ServiceException extends GenericException {

  private static final long serialVersionUID = -7657111991342365664L;

  private String errorCode;

  /**
   * Constructs the exception with the message.
   * 
   * @param message exception message
   */
  public ServiceException(String message) {
    this(message, null);
  }

  /**
   * Constructs with the supplied message & the cause.
   * 
   * @param message exception message
   * @param cause Cause of the exception
   */
  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs with the cause.
   * 
   * @param cause Cause of the exception
   */
  public ServiceException(Throwable cause) {
    this(cause.getMessage(), cause);
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }
  
  public String getErrorCode() {
    return this.errorCode;
  }
}
