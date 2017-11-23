package com.maqs.moneytracker.server.core.interceptor;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;


public class TimeLoggingInterceptor implements MethodInterceptor {

	private Logger logger = Logger.getLogger(getClass());
	
	public Object invoke(MethodInvocation invocation) throws Throwable {
		long start = System.currentTimeMillis();
		Object rval = invocation.proceed();
		long end = System.currentTimeMillis();
		long timeRequired = (end - start); 
		if (logger.isDebugEnabled()) {
			StringBuilder builder = new StringBuilder();
			builder.append("\n****************Time Log***************");
			builder.append("\nClass Name : " + invocation.getThis().getClass().getName());
			builder.append("\nMethod Called : " + invocation.getMethod().getName());
			builder.append("\nTime Required : " + timeRequired + " ms.");
			builder.append("\n***************************************");
			logger.debug(builder.toString());
		}
		return rval;
	}

}
