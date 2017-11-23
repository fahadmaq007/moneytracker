package com.maqs.moneytracker.common;

import com.maqs.moneytracker.common.paging.Page;

/**
 * The place to hold the constants.
 * @author maq
 *
 */
public final class Constansts {
	/**
	 * Constructor is private, just to avoid instantiation.
	 */
	private Constansts() {
	}
	/*
	 * Constants are defined here...
	 */
	/**
	 * Empty string.
	 */
	public static final String EMPTY_STRING = "";
	
	/**
	 * Space.
	 */
	public static final String SPACE = " ";
	
	/**
	 * Comma.
	 */
	public static final String COMMA = ",";

	/**
	 * Open square bracket.
	 */
	public static final String OPEN_BRACKET = "[";
	
	/**
	 * Close square bracket.
	 */
	public static final String CLOSE_BRACKET = "]";
	/**
	 * Percentage.
	 */
	public static final String PERCENTAGE = "%";
	/*
	 * End of Constants
	 */

	public static final Page DEFAULT_PAGE = new Page(1, 100);
	
}
