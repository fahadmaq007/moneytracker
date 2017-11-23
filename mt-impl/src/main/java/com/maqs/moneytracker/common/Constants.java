package com.maqs.moneytracker.common;


/**
 * The place to hold the constants.
 * @author maq
 *
 */
public final class Constants {
	/**
	 * Constructor is private, just to avoid instantiation.
	 */
	private Constants() {
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

	/*
	 * Actions
	 */
	public static final String HOME = "Home";

	public static final String TRANSACTION = "Transactions";

	public static final String APP_IMAGE = "Money";
	
	public static final String DELETE_IMAGE = "Delete";
	
	public static final String SAVE_IMAGE = "Save";

	public static final String TRANSACTION_BY_CAT = "Transactions By Category";

	public static final String DOLLAR = "Dollar";
	
	public static final int DEFAULT_FORM_FIELD_WIDTH = 200;

	public static final double DEFAULT_FORM_FIELD_HEIGHT = 35;
	
	public static final String[] MONTHS = {
		"Jan", "Feb", "Mar",
		"Apr", "May", "Jun",
		"Jul", "Aug", "Sep",
		"Oct", "Nov", "Dec"
	};

	public static final double VIEWPORT_HEIGHT = 999;

	public static final String HYPHEN = "-";

	public static final String USER_ID = "userId";
	
	public static final String SYSTEM_USER = "system.user";

	public static final String ACTIVITY_ACCT = "Accounts Created?";
	
	public static final String ACTIVITY_CAT = "Atleast 5 Categories available?";
	
	public static final String ACTIVITY_TRAN = "Transactions Created?";

	public static final String ACTIVITY_BUDGET = "Budget Created?";
	
	public static final String ACTIVITY_STATEMENTS = "Import Statements Created?";
	
	public static final int ACTIVITY_ACCT_POINTS = 25;
	
	public static final int ACTIVITY_CAT_POINTS = 25;
	
	public static final int ACTIVITY_TRAN_POINTS = 20;
	
	public static final int ACTIVITY_BUDGET_POINTS = 15;

	public static final int ACTIVITY_STATEMENT_POINTS = 15;
	
	public static final String ACTIVITY_TOTAL_RES = "Total {0} Available";

	public static final long ACTIVITY_CAT_MIN = 5;

	public static final String MSG_DUPLICATE = "Failed: Already Exists";
	
	public static final String MSG_POTENTIAL_DUPLICATE_TRANSACTION = "Potential Duplicate";

	public static final String MSG_OK_TO_SAVE = "Ok to Save";

	public static final String MSG_SUCCESSFUL = "Stored Successfully";
}
