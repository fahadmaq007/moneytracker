package com.maqs.moneytracker.types;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class AccountType {

	public static final String CASH = "C";
	
	public static final String BANK = "B";
	
	public static final String CREDIT_CARD = "CC";
	
	public static final String SODEXO = "S";
	
	public static final String DEBT = "D";

	private static final String HYPHEN = "-";
	
	private final String code;
	
	private final String name;
	
	public AccountType() {
		this(types.get(CASH).code, types.get(CASH).name);
	}
	public AccountType(String code, String name) {
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

	public static AccountType valueOf(String code) {
		return types.get(code);
	}
	
	private static Map<String, AccountType> types = new LinkedHashMap<String, AccountType>();
	
	static {
		AccountType cashType = new AccountType(CASH, "Cash");
		types.put(CASH, cashType); 
		
		AccountType bankType = new AccountType(BANK, "Bank");
		types.put(BANK, bankType); 
		
		AccountType sodexoType = new AccountType(SODEXO, "Sodexo");
		types.put(SODEXO, sodexoType); 
		
		AccountType ccType = new AccountType(CREDIT_CARD, "Credit Card");
		types.put(CREDIT_CARD, ccType); 
		/*
		AccountType debtType = new AccountType(DEBT, "Debt");
		types.put(DEBT, debtType); */
	}
	
	public static Collection<AccountType> values() {
		return types.values();
	}
	
	public static Collection<String> keys() {
		return types.keySet();
	}
}
