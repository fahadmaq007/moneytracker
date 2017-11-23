package com.maqs.moneytracker.common.webservice.adapter;

import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.DatatypeConverter;

/**
 * Adapter parse several complex types.
 *
 * @author maqbool.ahmed
 *
 */
//TODO This is a temporary class for handling date with Time Zone, 
//In future we may have to tell JAXB to support the time zone as well.
public class DataTypeAdapter {
	
	private DataTypeAdapter() {
		
	}
	
	/**
	 * Parses the string and returns the java date.
	 * @param s string to parse
	 * @return date
	 */
	public static Date parseDate(String s) {
        if (s == null) {
            return null;
        }
        return DatatypeConverter.parseDate(trim(s)).getTime();
    }
	
	private static String trim(String s) {
    	if (s.contains(".")) {
        	s = s.substring(0, s.indexOf("."));
        }
		return s;
	}
    
	/**
	 * Prints the date in a string format.
	 * @param dt date
	 * @return date string
	 */
	public static String printDate(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return DatatypeConverter.printDate(c);
    }

	/**
	 * Parses the string and returns the java time.
	 * @param s string to parse
	 * @return time
	 */
    public static Date parseTime(String s) {
        if (s == null) {
            return null;
        }
        return DatatypeConverter.parseTime(trim(s)).getTime();
    }
    
    /**
	 * Prints the time in a string format.
	 * @param dt time
	 * @return date string
	 */
    public static String printTime(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return DatatypeConverter.printTime(c);
    }

    /**
	 * Parses the string and returns the java timestamp.
	 * @param s string to parse
	 * @return date
	 */
    public static Date parseDateTime(String s) {
        if (s == null) {
            return null;
        }
        return DatatypeConverter.parseDateTime(trim(s)).getTime();
    }
    
    /**
	 * Prints the timestamp in a string format.
	 * @param dt timestamp
	 * @return date string
	 */
    public static String printDateTime(Date dt) {
        if (dt == null) {
            return null;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        return DatatypeConverter.printDateTime(c);
    }
}
