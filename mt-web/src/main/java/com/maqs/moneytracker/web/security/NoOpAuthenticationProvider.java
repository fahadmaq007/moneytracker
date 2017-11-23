package com.maqs.moneytracker.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class NoOpAuthenticationProvider implements AuthenticationProvider {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
    	logger.debug("authenticate: " + auth);
        return auth;
    }

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}
