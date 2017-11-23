package com.maqs.moneytracker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ColumnMap extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	public static final String BANK_ST_ID = "bankStatementId";
	
	private Long bankStatementId;
	
	private String propertyName;

	private String columnName;

	private String dataType;
	
	public ColumnMap() {

	}

	public ColumnMap(String propertyName, String columnName) {
		setPropertyName(propertyName);
		setColumnName(columnName);
	}

	public Long getBankStatementId() {
		return bankStatementId;
	}
	
	public void setBankStatementId(Long bankStatementId) {
		this.bankStatementId = bankStatementId;
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getDataType() {
		return dataType;
	}
	
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	
	@Override
	public String toString() {
		return getPropertyName() + ": " + getColumnName();
	}
}
