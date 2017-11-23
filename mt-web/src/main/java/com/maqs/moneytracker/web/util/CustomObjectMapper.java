package com.maqs.moneytracker.web.util;

import java.text.SimpleDateFormat;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class CustomObjectMapper extends ObjectMapper {

	/**
	 *
	 */
	private static final long serialVersionUID = -3572703163049203710L;

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final SimpleDateFormat df = new SimpleDateFormat("yyyy-MMM-dd");

	public CustomObjectMapper() {
		logger.debug(getClass() + " constructor");
		super.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		super.configure(SerializationFeature.INDENT_OUTPUT,true);
		super.setDateFormat(df);
	}

	@PostConstruct
	public void afterPropertiesSet() throws Exception {
		logger.debug("afterPropertiesSet: setting date format - " + df);
	}
}
