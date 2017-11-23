package com.maqs.moneytracker.common.service.exception;

import javax.xml.ws.WebFault;

/**
 * Denotes that the specified resource is not available.
 * 
 * @author Maqbool.Ahmed
 * 
 */
@WebFault(name = "InvalidInputException")
public class InvalidInputException extends GenericException {

  private static final long serialVersionUID = -7657111996782365664L;

  /**
   * Constructs with the supplied message & the cause. For Eg. throw new InvalidInputException("e.id,e.projectRef", id +
   * ",PRJ-001");
   * 
   * @param inputField field
   * @param inputValue value
   */
  public InvalidInputException(String inputField, Object inputValue) {
    this(inputField, inputValue, null);
  }

  /**
   * Constructs with the supplied message & the cause.
   * 
   * @param inputField field
   * @param inputValue value
   * @param cause cause
   */
  public InvalidInputException(String inputField, Object inputValue, Throwable cause) {
    super(inputField, cause);
    this.params = new Object[] {inputField, inputValue == null ? "null" : inputValue};
  }
}
