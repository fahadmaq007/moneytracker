package com.maqs.moneytracker.common.paging.spec;

import static com.maqs.moneytracker.common.Constansts.CLOSE_BRACKET;
import static com.maqs.moneytracker.common.Constansts.OPEN_BRACKET;

import java.io.Serializable;

/**
 * OrderBySpec defines the order by clause on the property.
 * 
 * @author maqbool.ahmed
 *
 */
public class OrderBySpec implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5631109974811514424L;

	private String propertyName;
	
	private OrderByOperation operation;
	
	public OrderBySpec() {
		
	}
	
	public OrderBySpec(String propertyName) {
		this(propertyName, OrderByOperation.ASC);
	}
	
	public OrderBySpec(String propertyName, OrderByOperation operation) {
		this.propertyName = propertyName;
		this.operation = operation;
	}

	public OrderByOperation getOperation() {
		return operation;
	}

	public void setOperation(OrderByOperation operation) {
		this.operation = operation;
	}

	public String getPropertyName() {
		return propertyName;
	}
	
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	
	@Override
	public String toString() {
		return OPEN_BRACKET + propertyName + "," + operation + CLOSE_BRACKET;
	}
}
