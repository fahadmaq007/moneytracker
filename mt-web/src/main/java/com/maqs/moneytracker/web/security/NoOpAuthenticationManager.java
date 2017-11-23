package com.maqs.moneytracker.web.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public class NoOpAuthenticationManager implements AuthenticationManager {
	private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
    	logger.debug(getClass() + " authenticate is been called: " + authentication);
    	SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
 
}
