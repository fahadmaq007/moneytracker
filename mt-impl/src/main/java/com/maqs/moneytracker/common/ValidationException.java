package com.maqs.moneytracker.common;

import com.maqs.moneytracker.common.service.exception.ServiceException;

public class ValidationException extends ServiceException {

	public ValidationException(String message) {
		super(message);
	}

}
