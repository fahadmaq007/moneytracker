package com.maqs.moneytracker.types;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * Periods 
 * 
 * @author Maqbool.Ahmed
 * 
 */
@JsonInclude(Include.NON_NULL)
public class Period {

	public static final String MONTHLY = "M";

	public static final String WEEKLY = "W";

	public static final String QUARTERLY = "Q";

	public static final String YEARLY = "Y";

	private String code;

	private String name;

	private int days;

	public Period() {
		this(MONTHLY);
	}
	
	public Period(String code) {
		Period t = valueOf(code);
		this.code = t.code;
		this.name = t.name;
		this.days = t.days;
	}
	
	public Period(String code, String name, int days) {
		this.code = code;
		this.name = name;
		this.days = days;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getDays() {
		return days;
	}
	
	public void setDays(int days) {
		this.days = days;
	}
	public static Period valueOf(String code) {
		return periods.get(code);
	}
	
	private static Map<String, Period> periods = new LinkedHashMap<String, Period>();
	
	static {
		Period monthly = new Period(MONTHLY, "Month(s)", 30);
		periods.put(MONTHLY, monthly); 
		Period weekly = new Period(WEEKLY, "Week(s)", 7);
		periods.put(WEEKLY, weekly); 
		Period quarterly = new Period(QUARTERLY, "Quarter(s)", monthly.days * 3);
		periods.put(QUARTERLY, quarterly);
		Period yearly = new Period(YEARLY, "Year(s)", 365);
		periods.put(YEARLY, yearly);
	}
	
	public static Collection<Period> values() {
		return periods.values();
	}
	
	public static Collection<String> keys() {
		return periods.keySet();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Period other = (Period) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
	
}
