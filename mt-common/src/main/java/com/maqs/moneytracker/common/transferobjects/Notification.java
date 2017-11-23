package com.maqs.moneytracker.common.transferobjects;

import com.google.gson.Gson;

public class Notification {

  private NotificationType notificationType;
  
  private String code;
  
  private String message;
  
  public Notification() {
    this(NotificationType.INFO);
  }
  
  public Notification(NotificationType notificationType) {
    this.notificationType = notificationType;
  }

  public NotificationType getNotificationType() {
    return notificationType;
  }

  public void setNotificationType(NotificationType notificationType) {
    this.notificationType = notificationType;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "Type: " + notificationType + ", Message: " + message;
  }

  public String toJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
