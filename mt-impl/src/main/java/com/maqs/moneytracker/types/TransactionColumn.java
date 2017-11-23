package com.maqs.moneytracker.types;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class TransactionColumn {

	public static final String ON_DATE = "onDate";
	
	public static final String DESC = "description";
	
	public static final String AMOUNT = "amount";
	
	public static final String TYPE_TEXT = "text";
	
	public static final String TYPE_DATE = "date";
	
	public static final String TYPE_DECIMAL = "decimal";
	
	public static final String TYPE_LONG = "long";

	public static final String CATEGORY_ID = "categoryId";

	public static final String ACCOUNT_ID = "accountId";
	
	public static final String FROM_ACCOUNT_ID = "fromAccountId";
	
	private String propName;

	private String displayName;

	private String dataType;
	
	public TransactionColumn(String propName, String displayName,
			String dataType) {
		this.propName = propName;
		this.displayName = displayName;
		this.dataType = dataType;
	}

	public String getPropName() {
		return propName;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Override
    public String toString() {
    	return getPropName();
    }

	public static TransactionColumn valueOf(String propName) {
		return columns.get(propName);
	}
	
	private static Map<String, TransactionColumn> columns = new LinkedHashMap<String, TransactionColumn>();
	
	static {
		TransactionColumn dateColumn = new TransactionColumn(ON_DATE, "Date", TYPE_DATE);
		columns.put(ON_DATE, dateColumn); 
		
		TransactionColumn descColumn = new TransactionColumn(DESC, "Description", TYPE_TEXT);
		columns.put(DESC, descColumn); 
		
		TransactionColumn amountColumn = new TransactionColumn(AMOUNT, "Amount", TYPE_DECIMAL);
		columns.put(AMOUNT, amountColumn);
		
		TransactionColumn categoryColumn = new TransactionColumn(CATEGORY_ID, "Category", TYPE_LONG);
		columns.put(CATEGORY_ID, categoryColumn);
		
		TransactionColumn accountColumn = new TransactionColumn(ACCOUNT_ID, "Account", TYPE_LONG);
		columns.put(ACCOUNT_ID, accountColumn);
		
		TransactionColumn fromAccountColumn = new TransactionColumn(FROM_ACCOUNT_ID, "From Account", TYPE_LONG);
		columns.put(FROM_ACCOUNT_ID, fromAccountColumn);
	}
	
	public static Collection<TransactionColumn> values() {
		return columns.values();
	}
	
	public static Collection<String> keys() {
		return columns.keySet();
	}
}
