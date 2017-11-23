package com.maqs.moneytracker.common.util;

/**
 * Utility methods are defined in this class to handle Numbers.
 * @author maq
 *
 */
public class NumberUtil {

	private NumberUtil() {
		
	}
	
	/**
	 * Checks whether the passed string is a Number.
	 * @param number number to be checked
	 * @return false if NaN otherwise true.
	 */
	public static boolean isNumber(String number) {
		if (number == null)
			return false;
		
		try {
			Long.valueOf(number);
		} catch (NumberFormatException e) {
			return false;
		}
		
		return true;
	}
}
