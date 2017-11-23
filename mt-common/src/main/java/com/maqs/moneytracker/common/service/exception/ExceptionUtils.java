package com.maqs.moneytracker.common.service.exception;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import com.maqs.moneytracker.common.Constansts;

/**
 * Exception Utils gives utility methods to help the exception classes.
 * @author maqbool.ahmed
 *
 */
public class ExceptionUtils {

	/**
	 * Builds the exception message with the given errorCode and the inputParams
	 * @param messagesBundle MessageBundle
	 * @param errorCode errorCode to look for in the messageBundle
	 * @param inputParams params to replace
	 * @return
	 */
	public static String buildMessage(ResourceBundle messagesBundle,
			String errorCode, Object... inputParams) {
		String resultMsg = null;
		if (messagesBundle != null) {
			String msg = messagesBundle.getString(errorCode);
			if (msg != null) {
				resultMsg = MessageFormat.format(msg, inputParams);
			}
		}
		return resultMsg == null ? Constansts.EMPTY_STRING : resultMsg;
	}
}
