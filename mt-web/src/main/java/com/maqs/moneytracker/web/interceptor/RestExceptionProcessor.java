package com.maqs.moneytracker.web.interceptor;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.maqs.moneytracker.common.service.exception.BusinessException;
import com.maqs.moneytracker.common.service.exception.ServiceException;
import com.maqs.moneytracker.common.service.exception.ValidationException;
import com.maqs.moneytracker.common.transferobjects.Notification;
import com.maqs.moneytracker.common.transferobjects.NotificationType;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.validation.InvalidInput;
import com.maqs.moneytracker.common.validation.ValidationError;

/**
 * Exception Handler for RestFul Services.
 * 
 * @author Maqbool.Ahmed
 * 
 */
@ControllerAdvice
public class RestExceptionProcessor {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private static final Object HAS_INVALID_DATA = "has invalid data: ";

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Notification processValidationError(MethodArgumentNotValidException ex) {
    BindingResult result = ex.getBindingResult();
    List<FieldError> fieldErrors = result.getFieldErrors();
    return processFieldErrors(fieldErrors);
  }

  private Notification processFieldErrors(List<FieldError> fieldErrors) {
    Notification notification = new Notification(NotificationType.ERROR);
    notification.setCode("MethodArgumentNotValid");
    StringBuilder message = new StringBuilder();
    for (FieldError fieldError : fieldErrors) {
      String field = fieldError.getField();
      String fieldMsg = fieldError.getDefaultMessage();
      message.append(field + ":" + fieldMsg + ", ");
    }
    notification.setMessage(message.toString());
    return notification;
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  Notification httpMessageNotReadable(HttpMessageNotReadableException ex) {
      Throwable mostSpecificCause = ex.getMostSpecificCause();
      Notification notification = new Notification(NotificationType.ERROR);
      if (mostSpecificCause != null) {
          String exceptionName = mostSpecificCause.getClass().getName();
          String message = mostSpecificCause.getMessage();
          notification.setCode(exceptionName);
          notification.setMessage(message);
      } else {
        notification.setMessage(ex.getMessage());
      }
      return notification;
  }  
  
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Notification requestMethodNotSupported(WebRequest req,
      HttpRequestMethodNotSupportedException ex) {
    return handleInternalServerError(ex, NotificationType.ERROR);
  }

  @ExceptionHandler(NoSuchRequestHandlingMethodException.class)
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public Notification requestHandlingMethodNotSupported(WebRequest req,
      NoSuchRequestHandlingMethodException ex) {
    return handleInternalServerError(ex, NotificationType.ERROR);
  }

  @ExceptionHandler(ValidationException.class)
  @ResponseBody
  public Notification handleValidationException(WebRequest req, ValidationException ex)
      throws ServletException, IOException {
    Notification notification = new Notification(NotificationType.VALIDATION);
    notification.setCode(ex.getErrorCode());
    ValidationError error = ex.getError();
    StringBuilder message = new StringBuilder();
    if (error != null) {
      List<InvalidInput> invalidInputList = error.getInputList();
      if (CollectionsUtil.isNonEmpty(invalidInputList)) {
        for (InvalidInput invalidInput : invalidInputList) {
          String field = invalidInput.getPropertyName();
          Object value = invalidInput.getPropertyValue();
          message.append(field).append(HAS_INVALID_DATA).append(value);
        }
      }
    }
    notification.setMessage(message.toString());
    return notification;
  }

  @ExceptionHandler(BusinessException.class)
  @ResponseBody
  public Notification handleBusinessException(WebRequest req, BusinessException ex)
      throws ServletException, IOException {
    return handleInternalServerError(ex, NotificationType.ERROR);
  }
  
  @ExceptionHandler(IllegalArgumentException.class)
  @ResponseBody
  public Notification handleIllegalArgumentException(WebRequest req,
      IllegalArgumentException ex) throws ServletException, IOException {
    return handleInternalServerError(ex, NotificationType.ERROR);
  }
  
  @ExceptionHandler(ServiceException.class)
  @ResponseBody
  public Notification handleServiceException(WebRequest req, ServiceException ex)
      throws ServletException, IOException {
    return handleInternalServerError(ex, NotificationType.ERROR);
  }

  @ExceptionHandler(Exception.class)
  @ResponseBody
  public Notification handleException(WebRequest req, Exception ex)
      throws ServletException, IOException {
    return handleInternalServerError(ex, NotificationType.ERROR);
  }

  private Notification handleInternalServerError(Exception ex, NotificationType type) {
    Notification notification = new Notification(type);
    notification.setCode(ex.getClass().getName());
    notification.setMessage(ex.getMessage());
    if (type == NotificationType.INFO) {
      logger.info(notification.toString(), ex);
    } else if (type == NotificationType.WARNING) {
      logger.warn(notification.toString(), ex);
    } else if (type == NotificationType.ERROR) {
      logger.error(notification.toString(), ex);
    } else {
      logger.debug(notification.toString(), ex);
    }
    return notification;
  }
}
