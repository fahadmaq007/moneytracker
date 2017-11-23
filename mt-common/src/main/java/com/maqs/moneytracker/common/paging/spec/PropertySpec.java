package com.maqs.moneytracker.common.paging.spec;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

import static com.maqs.moneytracker.common.Constansts.CLOSE_BRACKET;
import static com.maqs.moneytracker.common.Constansts.OPEN_BRACKET;

/**
 * PropertySpec defines the specifications on property.
 * For eg. propertyName LIKE %, the propertyName is to be evaluated with '%' by LIKE operation
 * @author maqbool.ahmed
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertySpec implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5631109974811514424L;

	private String propertyName;
	
	private Object value;
	
	private Operation operation;
	
	public PropertySpec() {
		this(null, null);
	}
	
	public PropertySpec(String propertyName, Object value) {
		this(propertyName, Operation.EQ, value);
	}
	
	public PropertySpec(String propertyName, Operation operation, Object value) {
		this.propertyName = propertyName;
		this.operation = operation;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
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
				",value=" + (value == null ? "null" : ToStringBuilder.reflectionToString(value)) + CLOSE_BRACKET;
	}
}
