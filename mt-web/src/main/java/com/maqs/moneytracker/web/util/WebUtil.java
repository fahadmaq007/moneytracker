package com.maqs.moneytracker.web.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpMethod;

public class WebUtil {

	/**
	 * Checks if this is a OPTIONS request.
	 * 
	 * @param request
	 * @return
	 */
	public static boolean isOptionsRequest(HttpServletRequest request) {
		return HttpMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod());
	}
	
}
