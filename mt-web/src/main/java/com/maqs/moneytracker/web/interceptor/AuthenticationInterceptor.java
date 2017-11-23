package com.maqs.moneytracker.web.interceptor;

import java.io.IOException;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.maqs.moneytracker.security.CustomAuthenticationToken;
import com.maqs.moneytracker.security.TokenManager;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
	
	private final Logger logger = LoggerFactory
			.getLogger(getClass());
		
	@Autowired
	private TokenManager tokenManager;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		try {
			super.preHandle(request, response, handler);
		} catch (Exception e) {
			e.printStackTrace();throw e;
		}
		Authentication authentication = attemptAuthentication(request, response);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		try {
		super.postHandle(request, response, handler, modelAndView);
		} catch (Exception e) {
			e.printStackTrace();throw e;
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}
	
	private Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		String header = TokenManager.TOKEN_HEADER;
		String uri = request.getRequestURI() + ":" + request.getMethod();
		logger.debug("authentication on " + uri);
		String token = request.getHeader(header);
		if (token == null) {
			logger.error("no token found with header: " + header);
		}
		AbstractAuthenticationToken userAuthenticationToken = authUserByToken(token);
		if (userAuthenticationToken == null) {
			throw new AuthenticationServiceException(MessageFormat.format(
					"Error | {0}", "Bad Token"));
		}		
		
		return userAuthenticationToken;
	}
	
	/**
	 * authenticate the user based on token
	 * 
	 * @return
	 */
	private AbstractAuthenticationToken authUserByToken(String token) {
		if (token == null) {
			return null;
		}
		AbstractAuthenticationToken authToken = null;
		try {
			UserDetails userDetails = tokenManager.getUserFromToken(token);
			if (userDetails == null) {
				throw new AuthenticationServiceException("Could not generate the user from token.");
			}
			authToken = new CustomAuthenticationToken(userDetails);
			return authToken;
		} catch (Exception e) {
			logger.error("Authenticate user by token error: " + e.getMessage(),
					e);
		}
		return authToken;
	}
}
