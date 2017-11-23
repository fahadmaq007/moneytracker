package com.maqs.moneytracker.common.service.exception;

import java.util.ResourceBundle;

import javax.xml.ws.WebFault;

import com.maqs.moneytracker.common.resourcebundle.MultipleResourceBundle;

/**
 * Base exception for all the exceptions.
 * 
 * @author Maqbool.Ahmed
 * 
 */
@WebFault(name = "GenericException")
public abstract class GenericException extends Exception {

  private static final long serialVersionUID = -7657342991371365664L;

  protected Object[] params;

  private static MultipleResourceBundle messagesBundle;

  /**
   * Constructs the exception with the message.
   * 
   * @param message exception message
   */
  public GenericException(String message) {
    this(message, null);
  }

  /**
   * Constructs with the supplied message & the cause.
   * 
   * @param message exception message
   * @param cause Cause of the exception
   */
  public GenericException(String message, Throwable cause) {
    super(message, cause);
    this.params = new Object[] {message};
  }

  /**
   * Constructs with the cause.
   * 
   * @param cause Cause of the exception
   */
  public GenericException(Throwable cause) {
    this(null, cause);
  }

  /**
   * Gets the message for the corresponding errorCode.
   */
  /*
   * public String getMessage() { return ExceptionUtils.buildMessage(messagesBundle, getErrorCode(), getParams()); }
   */

  public Object[] getParams() {
    return params;
  }

  public void setParams(Object[] params) {
    this.params = params;
  }

  /**
   * Loads all the resources in the ResourceBundle. This is start-up activity through spring beans (core-beans.xml).
   * 
   * @param resources
   */
  public static void loadResourceBundle(String[] resources) {
    try {
      messagesBundle = new MultipleResourceBundle(resources);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Gets the configured messageBundle
   * 
   * @return messageBundle
   */
  public static MultipleResourceBundle getMessagesBundle() {
    return messagesBundle;
  }

  /**
   * Gets the message for the corresponding errorCode.
   */
  public String getErrorMessage(String errorCode, Object... params) {
    return getErrorMessage(messagesBundle, errorCode, params);
  }

  /**
   * Gets the message for the corresponding errorCode.
   */
  public String getErrorMessage(ResourceBundle bundle, String errorCode, Object... params) {
    return ExceptionUtils.buildMessage(bundle, errorCode, params);
  }
}
