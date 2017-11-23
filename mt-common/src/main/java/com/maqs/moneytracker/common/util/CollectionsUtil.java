package com.maqs.moneytracker.common.util;

import java.util.Collection;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

/**
 * All the utility methods pertaining to Collection APIs will be here.
 * @author maqbool.ahmed
 *
 */
public class CollectionsUtil {
	private static Logger logger = Logger.getLogger(CollectionsUtil.class);
	
	private CollectionsUtil() {
		
	}
	
	/**
	 * Checks whether given collection is not null or non-empty.
	 * @param c Colletion instance
	 * @return true if not null or non-empty, otherwise false;
	 */
	public static boolean isNullOrEmpty(Collection c) {
		return c == null || c.isEmpty();
	}
	
	/**
	 * Checks whether given collection is not null or non-empty.
	 * @param c Colletion instance
	 * @return true if not null or non-empty, otherwise false;
	 */
	public static boolean isNonEmpty(Collection c) {
		return c != null && !c.isEmpty();
	}
	
	/**
	 * Creates a clone of the Collection instance.
	 * @param source collection
	 * @return clone of a collection
	 */
	public static Collection cloneList(Collection source) {
		if (source == null)
			return null;
		try {
			Collection newList = source.getClass().newInstance();
			PropertyUtils.copyProperties(newList, source);
			return newList;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}
		return null;
	}
}
