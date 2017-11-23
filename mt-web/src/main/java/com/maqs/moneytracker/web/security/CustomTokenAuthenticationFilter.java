package com.maqs.moneytracker.web.security;

import java.io.IOException;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.maqs.moneytracker.security.CustomAuthenticationToken;
import com.maqs.moneytracker.security.TokenManager;
import com.maqs.moneytracker.web.util.WebUtil;

public class CustomTokenAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomTokenAuthenticationFilter.class);

	@Autowired
	@Qualifier(value = "noOpAuthenticationManager")
	private AuthenticationManager authencationManager;

	@Autowired
	private TokenManager tokenManager;

	public CustomTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(
				defaultFilterProcessesUrl));
	}

	@PostConstruct
	public void afterPropertiesSet() {
		setAuthenticationManager(authencationManager);
		setAuthenticationSuccessHandler(new AuthSuccessHandler());
		setAuthenticationFailureHandler(new AuthFailureHandler());
//		setContinueChainBeforeSuccessfulAuthentication(true);
		super.afterPropertiesSet();
	}

	@Override
	protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		boolean requires = !WebUtil.isOptionsRequest(request);
		String uri = request.getRequestURI() + ":" + request.getMethod();
		logger.debug("requiresAuthentication on " + uri + "? " + requires);
		return requires;
	}

	/**
	 * Attempt to authenticate request - basically just pass over to another
	 * method to authenticate request headers
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		String header = TokenManager.TOKEN_HEADER;
		String uri = request.getRequestURI() + ":" + request.getMethod();
		logger.debug("authentication on " + uri);
		String token = request.getHeader(header);
		if (token == null) {
			logger.error("no token found with header: " + header);
		}
		logger.info("token found: " + token);
		AbstractAuthenticationToken userAuthenticationToken = authUserByToken(token);
		if (userAuthenticationToken == null) {
			throw new AuthenticationServiceException(MessageFormat.format(
					"Error | {0}", "Bad Token"));
		}
		
		return this.getAuthenticationManager().authenticate(userAuthenticationToken);
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

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse)res;
		placeResponseHeader(req, res);
		if (requiresAuthentication(request, (HttpServletResponse)res)) {
			attemptAuthentication(request, response);
		} else {
			chain.doFilter(req, res);
		}
	}

	private void placeResponseHeader(ServletRequest req, ServletResponse res) throws IOException, ServletException {
		HttpServletResponse httpResponse=(HttpServletResponse)res;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin,tn, X-Requested-With, Content-Type, Accept, Authorization");
        httpResponse.setHeader("Access-Control-Expose-Headers", "WWW-Authenticate,status,Authorization");
	}
}