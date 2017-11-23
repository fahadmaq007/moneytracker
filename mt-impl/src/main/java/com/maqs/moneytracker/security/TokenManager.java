package com.maqs.moneytracker.security;

import org.springframework.security.core.userdetails.UserDetails;

import com.maqs.moneytracker.common.service.exception.ServiceException;

public interface TokenManager {
	
	String TOKEN_HEADER = "tn";

	String newToken(UserDetails userDetails) throws ServiceException;

	boolean valid(String token) throws ServiceException;

	UserDetails getUserFromToken(String token) throws ServiceException; 
}
