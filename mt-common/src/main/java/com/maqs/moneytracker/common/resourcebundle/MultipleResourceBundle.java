package com.maqs.moneytracker.common.resourcebundle;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

import sun.util.ResourceBundleEnumeration;

import com.maqs.moneytracker.common.service.runtimeexception.MissingResourceException;

/**
 * Resource bundle to load from multiple resources.
 * @author Maqbool.Ahmed
 *
 */
@SuppressWarnings("restriction")
public class MultipleResourceBundle extends ResourceBundle {

	private Logger logger = Logger.getLogger(getClass());
	
	/**
	 * Creates a property resource bundle from an {@link java.io.InputStream
	 * InputStream}. The property file read with this constructor must be
	 * encoded in ISO-8859-1.
	 * 
	 * @param stream
	 *            an InputStream that represents a property file to read from.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public MultipleResourceBundle(String[] resources)
			throws MissingResourceException {
		for (String resource : resources) {
			try {
				ResourceBundle bundle = ResourceBundle.getBundle(resource);
				Enumeration<String> keys = bundle.getKeys();
				String key = null;
				while (keys.hasMoreElements()) {
					key = keys.nextElement();
					lookup.put(key, bundle.getObject(key));
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new MissingResourceException(e.getMessage());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object handleGetObject(String key) {
		if (key == null) {
			logger.error("key cannot be null");
			throw new NullPointerException();
		}
		return lookup.get(key);
	}

	/**
	 * Returns an <code>Enumeration</code> of the keys contained in this
	 * <code>ResourceBundle</code> and its parent bundles.
	 * 
	 * @return an <code>Enumeration</code> of the keys contained in this
	 *         <code>ResourceBundle</code> and its parent bundles.
	 * @see #keySet()
	 */
	public Enumeration<String> getKeys() {
		ResourceBundle parent = this.parent;
		return new ResourceBundleEnumeration(lookup.keySet(),
				(parent != null) ? parent.getKeys() : null);
	}

	/**
	 * Returns a <code>Set</code> of the keys contained <em>only</em> in this
	 * <code>ResourceBundle</code>.
	 * 
	 * @return a <code>Set</code> of the keys contained only in this
	 *         <code>ResourceBundle</code>
	 * @since 1.6
	 * @see #keySet()
	 */
	protected Set<String> handleKeySet() {
		return lookup.keySet();
	}

	// ==================privates====================

	private final Map<String, Object> lookup = new HashMap<String, Object>();
}
