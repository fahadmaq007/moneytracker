package com.maqs.moneytracker.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public AuthSuccessHandler() {
	}
	
    protected String determineTargetUrl(HttpServletRequest request,
            HttpServletResponse response) {
        String context = request.getContextPath();
        String fullURL = request.getRequestURI();
        String url = fullURL.substring(fullURL.indexOf(context)+context.length());
        return url;
    }
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request,
            HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String url = determineTargetUrl(request,response);
        logger.debug("onAuthenticationSuccess: " + url + " : " + request.getMethod());
    }
}
