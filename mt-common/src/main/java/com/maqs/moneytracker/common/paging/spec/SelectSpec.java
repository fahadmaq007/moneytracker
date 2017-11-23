package com.maqs.moneytracker.common.paging.spec;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import static com.maqs.moneytracker.common.Constansts.CLOSE_BRACKET;
import static com.maqs.moneytracker.common.Constansts.OPEN_BRACKET;

/**
 * SelectSpec defines the specifications on property to be selected.
 * For eg. employeeName would mean select employeeName from employee
 * @author maqbool.ahmed
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class SelectSpec implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5631109974811514424L;

	private String propertyName;
	
	private Operation operation;
	
	public SelectSpec() {
		this(null, null);
	}
	
	public SelectSpec(String propertyName, Operation operation) {
		this.propertyName = propertyName;
		this.operation = operation;
	}
	
	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
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
		return OPEN_BRACKET + propertyName + "," + operation + 
				CLOSE_BRACKET;
	}
}
