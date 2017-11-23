package com.maqs.moneytracker.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.maqs.moneytracker.common.Constants;
import com.maqs.moneytracker.managers.reports.Report;

/**
 * Utility class to make SOAP calls.
 * 
 * @author maqbool.ahmed
 * 
 */
public final class DateUtil {

	private static Logger logger = Logger.getLogger(DateUtil.class);

	public static final String DEFAULT_FORMAT = "yyyy-MMM-dd";

	private static final DateTimeFormatter dateFormatterWithoutTime = DateTimeFormat
			.forPattern(DEFAULT_FORMAT);

	public static final String MONTH_FORMAT = "MMM-yy";

	private static final DateTimeFormatter dateFormatterMonthYear = DateTimeFormat
			.forPattern(MONTH_FORMAT);

	private static final String HYPHEN = "-";

	private static final String SLASH = "/";

	private static final String[] DATE_FORMATS_WITH_SLASH = { DEFAULT_FORMAT,
			"dd/MMM/yy", "dd/MM/yyyy", "MM/dd/yyyy", "dd/MMM/yyyy",
			"yyyy/MM/dd", "yyyy/MMM/dd", "MMM/dd/yy", "dd/MM/yy", "MM/dd/yy" };

	private static String[] DATE_FORMATS_WITH_HYPHEN = new String[DATE_FORMATS_WITH_SLASH.length];

	private static final Map<String, SimpleDateFormat> sdfMap = new HashMap<String, SimpleDateFormat>();

	private static final String datesSplChrs = "[-/]+";

	static {
		int i = 0;
		for (String format : DATE_FORMATS_WITH_SLASH) {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			sdfMap.put(format, sdf);

			String formatWithHyphen = format.replaceAll(SLASH, HYPHEN);
			DATE_FORMATS_WITH_HYPHEN[i++] = formatWithHyphen;
			sdf = new SimpleDateFormat(formatWithHyphen);
			sdfMap.put(formatWithHyphen, sdf);
		}
	}

	private DateUtil() {

	}

	public static String getMonthYear(Date d) {
		return dateFormatterMonthYear.print(d.getTime());
	}

	public static XMLGregorianCalendar toXMLGregorianCalendar(Date date) {
		if (date == null) {
			throw new IllegalArgumentException("given date is null");
		}
		GregorianCalendar g = new GregorianCalendar();
		g.setTime(date);
		try {
			return DatatypeFactory.newInstance().newXMLGregorianCalendar(g);
		} catch (DatatypeConfigurationException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}

	public static Date getDateWithoutTime(Date date) {
		Date dateWithoutTime = null;
		DateTime d = dateFormatterWithoutTime
				.parseDateTime(getDateStringWithoutTime(date));
		dateWithoutTime = d.toDate();

		return dateWithoutTime;
	}

	public static String getDateStringWithoutTime(Date date) {
		return dateFormatterWithoutTime.print(date.getTime());
	}

	public static DateTimeFormatter getDateFormatterWithoutTime() {
		return dateFormatterWithoutTime;
	}

	public static DateTimeFormatter getDefaultDateFormatter() {
		return dateFormatterWithoutTime;
	}

	public static Date getDate(String text) throws ParseException {
		DateTimeFormatter sdf = getDefaultDateFormatter();
		DateTime dateWithoutTime = sdf.parseDateTime(text);
		return dateWithoutTime.toDate();
	}

	public static String getDateAsString(String text, String dateformat) {
		// create SimpleDateFormat object with source string date format
		SimpleDateFormat sdfSource = new SimpleDateFormat(dateformat);

		// parse the string into Date object
		Date date;
		try {
			date = sdfSource.parse(text);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage());
		}

		// parse the date into another format
		return dateFormatterWithoutTime.print(date.getTime());
	}

	public static Date getDate(String value, String dateFormat)
			throws ParseException {
		SimpleDateFormat sdf = sdfMap.get(dateFormat);
		if (sdf == null) {
			sdf = new SimpleDateFormat(dateFormat);
		}
		Date d = null;
		try {
			d = sdf.parse(value);
		} catch (Exception e) {
			String[] formats = value.contains(SLASH) ? DATE_FORMATS_WITH_SLASH
					: value.contains(HYPHEN) ? DATE_FORMATS_WITH_HYPHEN : null;
			if (formats != null) {
				for (String format : formats) {
					if (!format.equals(dateFormat)) {
						sdf = sdfMap.get(format);
						try {
							d = sdf.parse(value);
							if (d != null) {
								return d;
							}
						} catch (Exception ex) {
							continue;
						}
					}
				}
			}
		}
		return d;
	}

	public static int getInt(String val) {
		return Integer.parseInt(val);
	}

	public static boolean isDateFormatMatching(String value, String format) {
		if (value.length() != format.length())
			return false;

		if (!value.matches(datesSplChrs)) {
			return false;
		}
		return true;
	}

	public static Date[] getThisMonthsRange() {
		DateTime now = new DateTime();
		int dayOfMonth = now.getDayOfMonth();
		DateTime monthStart = now.minusDays(dayOfMonth - 1);
		Date[] range = { monthStart.toDateMidnight().toDate(), now.toDate() };
		return range;
	}

	public static Date[] getLastMonthRange() {
		DateTime now = new DateTime();
		int dayOfMonth = now.getDayOfMonth();
		DateTime lastMonth = now.minusMonths(1);
		DateTime lastMonthStart = lastMonth.minusDays(dayOfMonth - 1);
		int noOfDays = daysOfMonth(lastMonth);
		DateTime lastMonthEnd = lastMonthStart.plusDays(noOfDays - 1);
		Date[] range = { lastMonthStart.toDateMidnight().toDate(),
				lastMonthEnd.toDate() };
		return range;
	}

	public static Date[] getHistoricalRange(Integer noOfMonths) {
		DateTime now = new DateTime();
		int dayOfMonth = now.getDayOfMonth();

		DateTime start = now.minusMonths(noOfMonths);
		start = start.minusDays(dayOfMonth - 1);
		Date[] range = { start.toDateMidnight().toDate(), now.toDate() };
		return range;

	}

	public static Date[] getHistoricalReportRange() {
		return getHistoricalRange(Integer.valueOf(6));
	}

	public static int daysOfMonth(DateTime dt) {
		switch (dt.getMonthOfYear()) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;

		case 6:
		case 9:
		case 4:
		case 11:
			return 30;

		case 2:
			return 28; // you'll have to do isleap on your own, but it's easy,
						// and probably included somewhere
		}
		throw new IllegalArgumentException();
	}

	public static Date getThisMonthsDate(Date onDate) {
		Date d = new Date();
		d.setDate(onDate.getDate());
		return d;
	}

	private static final String[] MONTHS = { "Jan", "Feb", "Mar", "Apr", "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };

	public static String getMonth(Date onDate) {
		return MONTHS[onDate.getMonth()];
	}

	public static String getMonthNYear(Date onDate) {
		DateTime d = new DateTime(onDate);
		return MONTHS[d.getMonthOfYear() - 1] + Constants.HYPHEN + d.getYear();
	}

	public static boolean isBetweenDateRange(Date onDate, Date[] range) {
		DateTime date = new DateTime(onDate);
		boolean between = date.isAfter(range[0].getTime())
				&& date.isBefore(range[1].getTime());
		return between;
	}

	public static Date[] getRange(Report reportBy) {
		Date[] range = null;
		switch (reportBy) {
		case THIS_MONTH:
			range = getThisMonthsRange();
			break;
		case LAST_MONTH:
			range = getLastMonthRange();
			break;
		case OVERALL:
			range = getYearRange();
			break;
		default:
			throw new IllegalArgumentException(reportBy
					+ " no such report type.");
		}
		return range;
	}

	public static void main(String[] args) {
		try {
			Date d = getDate("04-Nov-2014", "dd-MMM-yyyy");
			System.out.println(d);
			System.out.println(getToDate(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	}

	public static String getFormattedDate(Date date) {
		return dateFormatterWithoutTime.print(date.getTime());
	}

	public static DateTimeFormatter getFormat() {
		return dateFormatterWithoutTime;
	}

	public static Date[] getYearRange() {
		DateTime now = new DateTime();
		DateTime start = now.minusMonths(12);
		Date[] range = { start.toDateMidnight().toDate(), now.toDate() };
		return range;
	}

	public static Date getToDate(Date toDate) {
		if (toDate == null) {
			return null;
		}
		DateTime to = new DateTime(toDate.getTime()).plusDays(1);
		DateTime toDateWithEndTime = to.withTimeAtStartOfDay().minusSeconds(1);
		return toDateWithEndTime.toDate();
	}
	
}
