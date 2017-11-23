package com.maqs.moneytracker.types;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Transaction Type
 * 
 * @author Maqbool.Ahmed
 * 
 */
public class TransactionType {

	public static final String EXPENSE = "E";

	public static final String INCOME = "I";

	public static final String TRANSFER = "T";

	public static final String ASSET_PURCHASE = "AP";

	public static final String ASSET_SALE = "AS";

	public static final String LIABILITY_ACQUISITION = "LA";

	public static final String LIABILITY_DISCARD = "LD";

	private static final String HYPHEN = "-";
	
	private String code;

	private String name;

	private boolean displayable;

	private boolean hasCategories;

	private Operation operation;

	public TransactionType() {
		this(EXPENSE);
	}
	
	public TransactionType(String code) {
		TransactionType t = valueOf(code);
		this.code = t.code;
		this.name = t.name;
		this.operation = t.operation;
		this.displayable = t.displayable;
		this.hasCategories = t.hasCategories;
	}
	
	public TransactionType(String code, String name, boolean displayable,
			boolean hasCategories, Operation operation) {
		this.code = code;
		this.name = name;
		this.displayable = displayable;
		this.hasCategories = hasCategories;
		this.operation = operation;
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

	public boolean isDisplayable() {
		return displayable;
	}

	public void setDisplayable(boolean displayable) {
		this.displayable = displayable;
	}

	public boolean hasCategories() {
		return hasCategories;
	}

	public void setHasCategories(boolean hasCategories) {
		this.hasCategories = hasCategories;
	}

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}

	public static TransactionType valueOf(String code) {
		return types.get(code);
	}
	
	private static Map<String, TransactionType> types = new LinkedHashMap<String, TransactionType>();
	
	static {
		TransactionType expType = new TransactionType("E", "Expense", true, true, Operation.DEDUCT);
		types.put(EXPENSE, expType); 
		TransactionType incType = new TransactionType("I", "Income", true, true, Operation.ADD);
		types.put(INCOME, incType); 
		TransactionType transferType = new TransactionType("T", "Transfer", true, true, Operation.ADD);
		types.put(TRANSFER, transferType);
		TransactionType apType = new TransactionType("AP", "Asset Purchase", false, true, Operation.DEDUCT);
		types.put(ASSET_PURCHASE, apType); 
		TransactionType asType = new TransactionType("AS", "Asset Sale", false, true, Operation.ADD);
		types.put(ASSET_SALE, asType); 
		TransactionType laType = new TransactionType("LA", "Liability Acquisition", false, true,
				Operation.DEDUCT);
		types.put(LIABILITY_ACQUISITION, laType); 
		TransactionType ldType = new TransactionType("LD", "Liability Discard", false, true, Operation.ADD);
		types.put(LIABILITY_DISCARD, ldType);
	}
	
	public static Collection<TransactionType> values() {
		return types.values();
	}
	
	public static Collection<String> keys() {
		return types.keySet();
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
		TransactionType other = (TransactionType) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}
	
	
}
