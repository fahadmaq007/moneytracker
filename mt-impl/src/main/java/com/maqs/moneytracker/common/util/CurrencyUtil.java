package com.maqs.moneytracker.common.util;

import java.util.Collection;
import java.util.Currency;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Utility class for currency.
 * This class loads the configured currencies & provides utility methods.
 * @author maqbool.ahmed
 *
 */
public final class CurrencyUtil {

	private static final Map<String, Currency> currencyMap = new LinkedHashMap<String, Currency>();
	
	private static Currency defaultCurrency = null;
	
	private static Logger logger = Logger.getLogger(CurrencyUtil.class);
	
	private CurrencyUtil() {
		
	}
	/**
	 * This is a start-up activity.
	 * The currencies will be loaded by the spring context (For Eg. bim-impl-beans.xml)
	 * @param currencies
	 */
	public static void loadCurrencies(String[] currencies) {
		logger.info("loading the currencies...");
		for (String currency : currencies) {
			currencyMap.put(currency, Currency.getInstance(currency));
		}
		if (currencies.length > 0)
		defaultCurrency = currencyMap.get(currencies[0]);
	}
	
	/**
	 * Gets the list of configured currencies.
	 * @return
	 */
	public static Collection<Currency> getCurrencyList() {		
		return currencyMap.values();
	}
	
	/**
	 * Gets the default currency code
	 * @return defaultCurrencyCode
	 */
	public static String getDefaultCurrencyCode() {
		return (defaultCurrency == null ? null : defaultCurrency.getCurrencyCode());
	}

	/**
	 * Gets the default currency
	 * @return defaultCurrency
	 */
	public static Currency getDefaultCurrency() {
		return defaultCurrency;
	}
	
	/**
	 * Gets the currency by currencyCode
	 * @return currencyCode
	 */
	public static Currency getCurrencyByCode(String currencyCode) {
		return currencyMap.get(currencyCode);
	}
}
