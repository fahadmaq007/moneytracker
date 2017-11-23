package com.maqs.moneytracker.web.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
@Qualifier(value = "httpAuthenticationEntryPoint")
public class HttpAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		String contentType = request.getContentType();
		logger.debug("contentType: " + contentType + " exception: "
				+ authException.getMessage());
		/*if (WebUtil.isOptionsRequest(request)) {
			response.setStatus(HttpServletResponse.SC_NO_CONTENT);
		} else {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Unauthorized");
		}*/
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
				"Unauthorized");
	}

	
}
