package com.maqs.moneytracker.common.validation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ValidationError implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1778516248881960339L;

	private final ValidationType type;

	 private List<InvalidInput> inputList;

	public ValidationError() {
		this(ValidationType.ERROR);
	}

	public ValidationError(ValidationType type) {
		this.type = type;
	}

	public List<InvalidInput> getInputList() {
		return inputList;
	}

	public void setInputList(List<InvalidInput> inputList) {
		this.inputList = inputList;
	}

	public ValidationType getType() {
		return type;
	}
	
	public void addInvalidInput(String propertyName, String errorCode, Object propertyValue) {
		addInvalidInput(new InvalidInput(propertyName, errorCode, propertyValue)); 
	}
	
	public void addInvalidInput(InvalidInput input) {
		if (inputList == null) {
			this.inputList = new ArrayList<InvalidInput>();
		}
		inputList.add(input);
	}
	
	public void removeInvalidInput(InvalidInput input) {
		inputList.remove(input);
	}
}
