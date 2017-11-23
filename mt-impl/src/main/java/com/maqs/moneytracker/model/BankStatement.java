package com.maqs.moneytracker.model;

import java.util.ArrayList;
import java.util.List;

public class BankStatement extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	public static final String NAME = "name";

	private String name;
	
	private Long bankAccountId;

	private Account bankAccount;

	private List<ColumnMap> columnMaps;

	private List<DataMap> dataMaps;

	private int startRow = 1;

	private int endRow;

	private String dateFormat = "dd/MM/yy";
	
	public BankStatement() {

	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Long getBankAccountId() {
		return bankAccountId;
	}

	public void setBankAccountId(Long bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	public Account getBankAccount() {
		return bankAccount;
	}

	public void setBankAccount(Account bankAccount) {
		this.bankAccount = bankAccount;
	}

	public List<ColumnMap> getColumnMaps() {
		return columnMaps;
	}

	public void setColumnMaps(List<ColumnMap> columnMaps) {
		this.columnMaps = columnMaps;
	}

	public List<DataMap> getDataMaps() {
		return dataMaps;
	}

	public void setDataMaps(List<DataMap> dataMaps) {
		this.dataMaps = dataMaps;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public String getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	public void addColumnMap(ColumnMap c) {
		if (columnMaps == null) {
			columnMaps = new ArrayList<ColumnMap>();
		}
		columnMaps.add(c);
	}

	public void addDataMap(DataMap d) {
		if (dataMaps == null) {
			dataMaps = new ArrayList<DataMap>();
		}
		dataMaps.add(d);
	}

}
