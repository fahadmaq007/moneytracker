package com.maqs.moneytracker.common.util;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;


public class DateDeserializer extends JsonDeserializer<Date>{

	@Override
	public Date deserialize(JsonParser parser, DeserializationContext arg1)
			throws IOException, JsonProcessingException {
		String date = parser.getValueAsString();
		Date d;
		try {
			d = DateUtil.getDate(date);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
		return d;
	}
	
}
