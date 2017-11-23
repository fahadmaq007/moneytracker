package com.maqs.moneytracker.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.maqs.moneytracker.common.paging.spec.Operation;

@JsonInclude(Include.NON_NULL)
public class DataMap extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	private Long bankStatementId;
	
	private String searchField;

	private Operation op = Operation.LIKE;

	private String searchText;

	private List<DataField> dataFields;
	
	public DataMap() {

	}

	public DataMap(String searchField, String searchText) {
		setSearchField(searchField);
		setSearchText(searchText);
	}
	
	public Long getBankStatementId() {
		return bankStatementId;
	}
	
	public void setBankStatementId(Long bankStatementId) {
		this.bankStatementId = bankStatementId;
	}
	
	public String getSearchField() {
		return searchField;
	}

	public void setSearchField(String searchField) {
		this.searchField = searchField;
	}

	public Operation getOp() {
		return op;
	}

	public void setOp(Operation op) {
		this.op = op;
	}

	public String getSearchText() {
		return searchText;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

	public List<DataField> getDataFields() {
		return dataFields;
	}
	
	public void setDataFields(List<DataField> dataFields) {
		this.dataFields = dataFields;
	}
	
	public void addDataField(DataField d) {
		if (dataFields == null) {
			dataFields = new ArrayList<DataField>();
		}
		dataFields.add(d);
	}
	
	@Override
	public String toString() {
		return getSearchField() + ": " + getSearchText();
	}
}
