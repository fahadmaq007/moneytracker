package com.maqs.moneytracker.common.paging.spec;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * Operation defines the restriction to be applied on the property.
 * It closely works with {@link PropertySpec}
 * @author maqbool.ahmed
 *
 */
@XmlType(name = "operation")
@XmlEnum
public enum Operation {
	
	/**
	 * Like '%'.
	 */
	@XmlEnumValue("LIKE")
	LIKE,	
	/**
	 * Equal.
	 */
	@XmlEnumValue("EQ")
	EQ, 
	/**
	 * Less Than.
	 */
	@XmlEnumValue("LT")
	LT, 
	
	/**
	 * Greater Than.
	 */
	@XmlEnumValue("GT")
	GT,
	
	/**
	 * Less Than or Equal.
	 */
	@XmlEnumValue("LTE")
	LTE,
	
	/**
	 * Greater Than or Equal.
	 */
	@XmlEnumValue("GTE")
	GTE,
	
	/**
	 * Not Equal.
	 */
	@XmlEnumValue("NOTEQUAL")
	NOTEQUAL,
	
	/**
	 * Between Range usually for dates or timestamps.
	 */
	@XmlEnumValue("BETWEEN")
	BETWEEN,
	
	/**
	 * Is Null?.
	 */
	@XmlEnumValue("ISNULL")
	ISNULL,
	
	/**
	 * Is Not Null?.
	 */
	@XmlEnumValue("IS_NOT_NULL")
	IS_NOT_NULL,
	/**
	 * IN
	 */
	@XmlEnumValue("IN")
	IN,
	
	/**
	 * AVG
	 */
	@XmlEnumValue("AVG")
	AVG,
	
	/**
	 * SUM
	 */
	@XmlEnumValue("SUM")
	SUM,
	
	/**
	 * NOT IN
	 */
	@XmlEnumValue("NOT_IN")
	NOT_IN,
	
}
