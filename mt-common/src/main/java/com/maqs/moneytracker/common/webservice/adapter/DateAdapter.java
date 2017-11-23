package com.maqs.moneytracker.common.webservice.adapter;

import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * Date Adapter which marshals and unmarshals the string text to {@link}java.util.Date
 * 
 * @author maqbool.ahmed
 *
 */
public class DateAdapter extends XmlAdapter<String, Date> {

	/**
	 * Unmarshal the text to java date.
	 */
	public Date unmarshal(String value) {
		return (DataTypeAdapter.parseDateTime(value));
	}

	/**
	 * Marshal the java date to string.
	 */
	public String marshal(Date value) {
		return (DataTypeAdapter.printDateTime(value));
	}
}
