package com.maqs.moneytracker.common.service.exception;

import com.maqs.moneytracker.common.validation.ValidationError;

/**
 * Denotes the ValidationException that will have the details of validation errors.
 * This exception will be sent to the user.
 * 
 * @author Maqbool.Ahmed
 * 
 */
public class ValidationException extends ServiceException {

  private static final long serialVersionUID = -7657111991342365664L;

  private final ValidationError error;

  /**
   * Constructs the exception with the message.
   * 
   * @param message exception message
   */
  public ValidationException(String message, ValidationError error) {
    super(message);
    this.error = error;
  }

  public ValidationError getError() {
    return error;
  }
}
