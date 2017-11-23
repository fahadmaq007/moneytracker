package com.maqs.moneytracker.types;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class RecurringType {

	public static final String WEEKLY = "W";
	
	public static final String MONTHLY = "M";
	
	public static final String QUARTERLY = "Q";
	
	public static final String YEARLY = "Y";
		
	private final String code;
	
	private final String name;
	
	public RecurringType() {
		this(types.get(MONTHLY).code, types.get(MONTHLY).name);
	}
	
	public RecurringType(String code, String name) {
		this.code = code;
		this.name = name;
	}
	
	public String getCode() {
		return code;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
    public String toString() {
    	return getCode();
    }
    
	private static Map<String, RecurringType> types = new LinkedHashMap<String, RecurringType>();
	
	static {
		RecurringType weekType = new RecurringType(WEEKLY, "Week");
		types.put(WEEKLY, weekType); 
		
		RecurringType monthType = new RecurringType(MONTHLY, "Month");
		types.put(MONTHLY, monthType); 
		
		RecurringType quarterType = new RecurringType(QUARTERLY, "Quarter");
		types.put(QUARTERLY, quarterType); 
		
		RecurringType yearType = new RecurringType(YEARLY, "Year");
		types.put(YEARLY, yearType); 
	}
	
	public static Collection<RecurringType> values() {
		return types.values();
	}
	
	public static Collection<String> keys() {
		return types.keySet();
	}
}
