package com.maqs.moneytracker.server.core.interceptor;

import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

import com.maqs.moneytracker.common.service.exception.ServiceException;

/**
 * Exception Logging Mechanism. This class is an interceptor which will be
 * intercepted will the Business Layer APIs. The interceptor will log the error
 * and throw it again. The advantage of using this API is the exception handling
 * mechanism is at one place. None of the business layer APIs would catch the
 * business level checked exceptions, rather it will be done through the
 * interceptor.
 * 
 * @author maqbool.ahmed
 * 
 */
public class ExceptionLoggingInterceptor implements MethodInterceptor {

	private Logger logger = Logger.getLogger(getClass());

	public Object invoke(MethodInvocation invocation) throws Throwable {
		Method method = invocation.getMethod();
		String methodLocation = null;

		try {
			methodLocation = invocation.getMethod().getDeclaringClass()
					.getName()
					+ "." + method.getName();
			return invocation.proceed();
		} catch (ServiceException e) {
			logger.error(methodLocation
					+ " has thrown the service exception... " + e.getMessage(),
					e);
			throw e;
		} catch (Throwable e) {

			logger.error(
					methodLocation + " has thrown the exception... "
							+ e.getMessage(), e);

			throw new ServiceException(
					"Internal server error, please contact the administrator... ",
					e);

		}

	}

}
