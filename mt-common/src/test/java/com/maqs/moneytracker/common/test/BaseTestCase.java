package com.maqs.moneytracker.common.test;

import java.io.FileReader;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

/**
 * Base test case
 * Every test case should be extending the base.
 * @author Maqbool.Ahmed
 *
 */
public class BaseTestCase {

	protected Logger logger = Logger.getLogger(getClass());
	
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * Generates the XML string of the object.
	 * @param o
	 * @return
	 */
	protected String marshal(Object o) {
		StringBuilder buffer = new StringBuilder();
		try {
			JAXBContext context = JAXBContext.newInstance(o.getClass());
			Marshaller m = context.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.marshal(o, System.out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buffer.toString();
	}

	/**
	 * Un-marshals the object by reading the file.
	 * @param clazz to which unmarshalls to
	 * @param xmlPath xml file path
	 * @return
	 */
	protected Object unmarshal(Class clazz, String xmlPath) {
		Object o = null;
		FileReader reader = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Unmarshaller u = context.createUnmarshaller();
			reader = new FileReader(xmlPath);
			o = u.unmarshal(reader);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return o;
	}
}
