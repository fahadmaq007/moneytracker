package com.maqs.moneytracker.web.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResponseHeaderFilter implements Filter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException {
		HttpServletRequest r = (HttpServletRequest) request;
		String uri = r.getRequestURI() + ":" + r.getMethod();
		logger.debug(uri);
		HttpServletResponse httpResponse=(HttpServletResponse)response;
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin,tn, X-Requested-With, Content-Type, Accept, Authorization");
        httpResponse.setHeader("Access-Control-Expose-Headers", "WWW-Authenticate,status,Authorization");
        chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
