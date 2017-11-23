package com.maqs.moneytracker.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.transferobjects.Action;
import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.model.BaseEntity;

/**
 * This class provides Utility methods.
 * 
 * @author maqbool.ahmed
 * 
 */
public class AppUtil {
	private static final String DOT = ",";

	private static final String GET = "get";

	private static Logger logger = Logger.getLogger(AppUtil.class);

	private AppUtil() {

	}

	public static BaseEntity createEntity(QuerySpec querySpec) {
		BaseEntity entity = null;
		try {
			Class<? extends BaseEntity> eClass = getClass(querySpec);
			entity = eClass.newInstance();
			clearData(entity);
			for (PropertySpec prop : querySpec.getPropertySpecs()) {
				Object value = prop.getValue();
				String propName = prop.getPropertyName();
				Field field = eClass.getDeclaredField(propName);
				field.setAccessible(true);
				field.set(entity, value);
			}
		} catch (Exception e) {
			logger.error(e, e.getCause());
			e.printStackTrace();
		}
		return entity;
	}

	public static void clearData(BaseEntity entity) {
		try {
			Class<? extends BaseEntity> eClass = entity.getClass();
			for (Field field : eClass.getDeclaredFields()) {
				field.setAccessible(true);
				if (field.getModifiers() != 26 || field.getModifiers() != 25)
					field.set(entity, null);
			}
		} catch (Exception e) {
			logger.error(e, e.getCause());
			e.printStackTrace();
		}
	}

	public static Class<? extends BaseEntity> getClass(QuerySpec querySpec) {
		String className = querySpec.getClassName();
		try {
			return (Class<? extends BaseEntity>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			logger.error(e, e.getCause());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Copies the properties available in source to destination.
	 * 
	 * @param dest
	 *            destination object
	 * @param source
	 *            source object.
	 * @return Returns the destination
	 */
	public static Object copyProperties(Object dest, Object source) {
		try {
			PropertyUtils.copyProperties(dest, source);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return dest;
	}

	/**
	 * Creates clone of given object
	 * 
	 * @param source
	 *            given source
	 * @return destination (clone)
	 */
	public static Object cloneOf(Object source) {
		if (source == null)
			throw new IllegalArgumentException(
					"cannot create a copy as the source is null");

		Object dest = null;
		try {
			dest = createNewInstanceAs(source);
			PropertyUtils.copyProperties(dest, source);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return dest;
	}

	/**
	 * Creates a new instance of source's class.
	 * 
	 * @param source
	 * @return
	 * @throws Exception
	 */
	private static Object createNewInstanceAs(Object source) throws Exception {
		Class clazz = source.getClass();
		return clazz.newInstance();
	}

	/**
	 * Invokes the method & returns the updated entity.
	 * 
	 * @param entity
	 *            the current object
	 * @param methodName
	 *            method to be called
	 * @param parameterTypes
	 *            Parameter types
	 * @param args
	 *            list of argument objects
	 * @return Returns the return object of the invoked method.
	 */
	public static Object invokeMethod(Object entity, String methodName,
			Class[] parameterTypes, Object... args) {
		if (entity == null) {
			throw new IllegalArgumentException("entity cannot be null");
		}
		Method m;
		try {
			m = entity.getClass().getDeclaredMethod(methodName, parameterTypes);
			m.invoke(entity, args);
			return entity;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * Loops through the parentList & resolves the oneToMany relationship.
	 * 
	 * @param parentList
	 *            Parent list
	 * @param childMap
	 *            Map<parentId, List<Child>>
	 * @param parentMethodToSetChildren
	 *            parent's method name
	 * @param parameterTypes
	 *            method's parameter types
	 */
	public static List<? extends BaseEntity> resolveOneToMany(
			List<? extends BaseEntity> parentList,
			Map<Long, List<? extends BaseEntity>> childMap,
			String parentMethodToSetChildren, Class[] parameterTypes) {
		for (BaseEntity parent : parentList) {
			Long parentId = parent.getId();
			List<BaseEntity> childList = (List<BaseEntity>) childMap
					.get(parentId);
			resolveOneToMany(parent, childList, parentMethodToSetChildren,
					parameterTypes);
		}
		return parentList;
	}

	/**
	 * Invokes the parent's method to set the childList
	 * 
	 * @param parent
	 *            Parent entity
	 * @param childList
	 *            children
	 * @param parentMethodToSetChildren
	 *            parent's method name
	 * @param parameterTypes
	 *            method's parameter types
	 * @return parent entity
	 */
	public static BaseEntity resolveOneToMany(BaseEntity parent,
			List<? extends BaseEntity> childList,
			String parentMethodToSetChildren, Class[] parameterTypes) {
		if (childList != null) {
			parent = (BaseEntity) invokeMethod(parent,
					parentMethodToSetChildren, parameterTypes, childList);
		}
		return parent;
	}

	/**
	 * Invokes the parent's method to set the child
	 * 
	 * @param parent
	 *            Parent entity
	 * @param child
	 *            child entity
	 * @param parentMethodToSetChildren
	 * @return parent entity
	 */
	public static BaseEntity resolveOneToOne(BaseEntity parent,
			BaseEntity child, String parentMethodToSetChild) {
		if (child != null) {
			Class[] parameterTypes = new Class[] { child.getClass() };
			parent = (BaseEntity) invokeMethod(parent, parentMethodToSetChild,
					parameterTypes, child);
		}
		return parent;
	}

	/**
	 * Creates a map<Id, Entity>
	 * 
	 * @param entities
	 *            List<Entity>
	 * @return the map
	 */
	public static Map<Long, BaseEntity> getMap(
			List<? extends BaseEntity> entities) {
		Map<Long, BaseEntity> map = new HashMap<Long, BaseEntity>();
		if (entities == null)
			return map;
		for (BaseEntity entity : entities) {
			map.put(entity.getId(), entity);
		}
		return map;
	}

	/**
	 * Creates a set<Id>
	 * 
	 * @param entities
	 *            List<Entity>
	 * @return a set
	 */
	public static Set<Long> getIds(List<? extends BaseEntity> entities) {
		Set<Long> entityIds = new HashSet<Long>();
		for (BaseEntity e : entities) {
			entityIds.add(e.getId());
		}
		return entityIds;
	}

	/**
	 * Creates a set<Id>
	 * 
	 * @param entities
	 *            List<Entity>
	 * @return a set
	 */
	public static Set<Long> getIds(Collection<? extends BaseEntity> entities) {
		Set<Long> entityIds = new HashSet<Long>();
		for (BaseEntity e : entities) {
			entityIds.add(e.getId());
		}
		return entityIds;
	}

	public static List<? extends BaseEntity> resolveOneToMany(
			List<? extends BaseEntity> parentList,
			List<? extends BaseEntity> childList,
			String parentMethodToSetChildren, Class[] parameterTypes) {
		for (BaseEntity e : parentList) {
			resolveOneToMany(e, childList, parentMethodToSetChildren,
					parameterTypes);
		}
		return parentList;
	}

	public static Object getFieldValue(Object entity, String propName) {
		Object value = null;
		try {
			if (propName.contains(DOT)) {
				String[] params = propName.split(DOT);
				for (int i = 0, n = params.length; i < n; i++) {
					String param = params[i];
					entity = getFieldValue0(entity, param);
				}
				value = entity;
			} else {
				value = getFieldValue0(entity, propName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	private static Object getFieldValue0(Object o, String propName) {
		try {
			String methodName = GET + StringUtils.capitalize(propName);
			Method m = o.getClass().getMethod(methodName, null);
			return m.invoke(o, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getFormattedAmount(Number amount) {
		if (amount == null) {
			amount = BigDecimal.valueOf(0.0);
		}
		String formattedText = numberFormatter.format(amount);
		return formattedText;
	}

	private static final NumberFormat numberFormatter = NumberFormat
			.getNumberInstance();

	public static Number getAmount(String text) throws ParseException {
		if (text == null) {
			return BigDecimal.valueOf(0.0);
		}
		Number amount = null;
		amount = numberFormatter.parse(text);
		if (amount != null) {
			amount = new BigDecimal(amount.doubleValue());
		}

		return amount;
	}

	public static void removePropertySpec(QuerySpec querySpec,
			String propertyName) {
		List<PropertySpec> propertySpecs = querySpec.getPropertySpecs();

		for (PropertySpec propertySpec : propertySpecs) {
			String pName = propertySpec.getPropertyName();
			if (pName != null && pName.equals(propertyName)) {
				querySpec.getPropertySpecs().remove(propertySpec);
				break;
			}
		}
	}

	public static String getCurrencySymbol() {
		return Currency.getInstance(Locale.getDefault()).getSymbol();
	}

	public static String getFormattedText(String pattern, Object[] arguments) {
		return MessageFormat.format(pattern, arguments);
	}

	public static void setDeleteAction(Entity e) {
		e.getAction().setActionIndex(Action.DELETE);
	}

	public static void setUpdateAction(Entity e) {
		e.getAction().setActionIndex(Action.UPDATE);
	}

	public static void setDoNothingAction(Entity e) {
		e.getAction().setActionIndex(Action.DO_NOTHING);
	}

	public static void setNewAction(Entity e) {
		e.getAction().setActionIndex(Action.CREATE_NEW);
	}

	public static boolean isNumber(String amountText) {
		try {
			numberFormatter.parse(amountText);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static void updatePropertySpec(QuerySpec querySpec,
			String propertyName, Long value) {
		List<PropertySpec> propertySpecs = querySpec.getPropertySpecs();

		boolean found = false;
		for (PropertySpec propertySpec : propertySpecs) {
			String pName = propertySpec.getPropertyName();
			if (pName != null && pName.equals(propertyName)) {
				propertySpec.setValue(value);
				found = true;
				break;
			}
		}
		if (!found) {
			querySpec.addPropertySpec(new PropertySpec(propertyName, value));
		}
	}



	public static String getMessage(String msg, Object[] params) {
		return MessageFormat.format(msg, params);
	}

	/**
	 * Invokes the method & returns the updated entity.
	 * 
	 * @param entity
	 *            the current object
	 * @param methodName
	 *            method to be called
	 * @param parameterTypes
	 *            Parameter types
	 * @param args
	 *            list of argument objects
	 * @return Returns the return object of the invoked method.
	 */
	public static void setFieldValue(Object object, String fieldName,
			Object fieldValue) {
		Class<?> clazz = object.getClass();
		while (clazz != null) {
			try {
				Field field = clazz.getDeclaredField(fieldName);
				field.setAccessible(true);
				field.set(object, fieldValue);
				break;
			} catch (NoSuchFieldException e) {
				clazz = clazz.getSuperclass();
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		}
	}

	public static String createChecksum(String text) {
		return DigestUtils.md5Hex(text);
	}

	public static boolean matchChecksum(String text, String checksum) {
		String c = DigestUtils.md5Hex(text);
		return checksum.equals(c);
	}
}
