package com.maqs.moneytracker.common.validation;

import java.io.Serializable;

public class InvalidInput implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1778516188881960339L;

	private final String propertyName;
	
	private final String errorMessageCode;
	
	private final Object propertyValue;
	
	public InvalidInput(String propertyName, String errorMessageCode, Object propertyValue) {
		this.propertyName = propertyName;
		this.errorMessageCode = errorMessageCode;
		this.propertyValue = propertyValue;
	}
	
	public String getPropertyName() {
		return propertyName;
	}
	
	public Object getPropertyValue() {
		return propertyValue;
	}
	
	public String getErrorMessageCode() {
		return errorMessageCode;
	}
}
