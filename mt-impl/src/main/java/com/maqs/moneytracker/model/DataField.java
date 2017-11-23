package com.maqs.moneytracker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class DataField extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7170698571531437511L;

	public static final String DATA_MAP_ID = "dataMapId";

	private Long dataMapId;
	
	private String propertyName;

	private Object data;

	private String dataAsString;
	
	private String dataType;

	public DataField() {

	}

	public DataField(String propertyName, Object data, String dataType) {
		setPropertyName(propertyName);
		setData(data);
		setDataType(dataType);
	}

	public Long getDataMapId() {
		return dataMapId;
	}
	
	public void setDataMapId(Long dataMapId) {
		this.dataMapId = dataMapId;
	}
	
	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
		if (data != null) {
			this.dataAsString = data.toString();
		}
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataAsString() {
		return dataAsString;
	}
	
	public void setDataAsString(String dataAsString) {
		this.dataAsString = dataAsString;
	}
	
	@Override
	public String toString() {
		return getPropertyName() + ": " + getData();
	}
}
