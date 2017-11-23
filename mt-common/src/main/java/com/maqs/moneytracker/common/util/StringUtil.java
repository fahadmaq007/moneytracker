package com.maqs.moneytracker.common.util;

/**
 * Utility methods are defined in this class to handle Numbers.
 * @author maq
 *
 */
public class StringUtil {

	private StringUtil() {
		
	}
	
	/**
	 * Checks whether the passed string is a Number.
	 * @param text number to be checked
	 * @return false if NaN otherwise true.
	 */
	public static boolean nullOrEmpty(String text) {
		return text == null || text.length() == 0;
	}
}
