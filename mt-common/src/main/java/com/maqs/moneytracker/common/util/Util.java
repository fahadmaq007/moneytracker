package com.maqs.moneytracker.common.util;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;

import com.maqs.moneytracker.common.transferobjects.Action;
import com.maqs.moneytracker.common.transferobjects.Entity;

/**
 * This class provides Utility methods.
 * 
 * @author maqbool.ahmed
 * 
 */
public class Util {
	private static Logger logger = Logger.getLogger(Util.class);

	private Util() {

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
	public static List<? extends Entity> resolveOneToMany(
			List<? extends Entity> parentList,
			Map<Long, List<? extends Entity>> childMap,
			String parentMethodToSetChildren, Class[] parameterTypes) {
		for (Entity parent : parentList) {
			Long parentId = parent.getId();
			List<Entity> childList = (List<Entity>) childMap.get(parentId);
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
	public static Entity resolveOneToMany(Entity parent,
			List<? extends Entity> childList, String parentMethodToSetChildren,
			Class[] parameterTypes) {
		if (childList != null) {
			parent = (Entity) invokeMethod(parent, parentMethodToSetChildren,
					parameterTypes, childList);
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
	public static Entity resolveOneToOne(Entity parent, Entity child,
			String parentMethodToSetChild) {
		if (child != null) {
			Class[] parameterTypes = new Class[] { child.getClass() };
			parent = (Entity) invokeMethod(parent, parentMethodToSetChild,
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
	public static Map<Long, Entity> getMap(List<? extends Entity> entities) {
		Map<Long, Entity> map = new HashMap<Long, Entity>();
		if (entities == null)
			return map;
		for (Entity entity : entities) {
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
	public static Set<Long> getIds(List<? extends Entity> entities) {
		Set<Long> entityIds = new HashSet<Long>();
		for (Entity e : entities) {
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
	public static Set<Long> getIds(Collection<? extends Entity> entities) {
		Set<Long> entityIds = new HashSet<Long>();
		for (Entity e : entities) {
			entityIds.add(e.getId());
		}
		return entityIds;
	}

	public static List<? extends Entity> resolveOneToMany(
			List<? extends Entity> parentList,
			List<? extends Entity> childList, String parentMethodToSetChildren,
			Class[] parameterTypes) {
		for (Entity e : parentList) {
			resolveOneToMany(e, childList, parentMethodToSetChildren,
					parameterTypes);
		}
		return parentList;
	}

	/**
	 * Sets the Action.CREATE_NEW or Action.UPDATE recursively 
	 * for all the instances which entends Entity.class
	 * @param original Current state in the database
	 * @param updated Edited one.
	 */
	public static void setCreateOrUpdateAction(Entity original,
			Entity updated) {
		if (original == null || updated == null) {
			throw new IllegalArgumentException(
					"checking for diff & the entities cannot be null");
		}
		if (original.getClass() != updated.getClass()) {
			throw new IllegalArgumentException(
					"checking for diff on two different types...cannot be done.");
		}
		if (updated.getId() == null) {
			updated.getAction().setActionIndex(Action.CREATE_NEW);
			return;
		}
		try {
			Field[] fields = original.getClass().getDeclaredFields();
			for (Field field : fields) {
				field.setAccessible(true);
				Class clazz = field.getType();
				handleCollectionObjects(field, clazz, original, updated);
				handleEntityObjects(field, clazz, original, updated);
				if (!Entity.class.isAssignableFrom(clazz)
						&& !Collection.class.isAssignableFrom(clazz)) {
					Object originalValue = field.get(original);
					Object updatedValue = field.get(updated);
					boolean origNullUpdatedNotNull = originalValue == null && updatedValue != null;
					boolean origNotNullUpdatedNull = originalValue != null && updatedValue == null;
					boolean neitherNullNorEqual = originalValue != null && 
							updatedValue != null && !originalValue.equals(updatedValue);
					if (origNullUpdatedNotNull || origNotNullUpdatedNull || neitherNullNorEqual) {
						updated.getAction().setActionIndex(Action.UPDATE);
						break;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	/**
	 * Handles the Entity type objects
	 * Checks whether entity1 & entity2 has any changes, 
	 * if so updates the action as UPDATE
	 * @param field
	 * @param clazz
	 * @param original
	 * @param updated
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void handleEntityObjects(Field field, Class clazz,
			Entity original, Entity updated) 
					throws IllegalArgumentException, IllegalAccessException {
		if (Entity.class.isAssignableFrom(clazz)) {
			Entity originalEntity = (Entity) field.get(original);
			Entity updatedEntity = (Entity) field.get(updated);
			if (originalEntity != null && updatedEntity != null) {
				setCreateOrUpdateAction(originalEntity, updatedEntity);
			}
		}
	}

	/**
	 * Handles the Collection type objects
	 * Checks whether List<Entity>1 & List<Entity>2 has any changes, 
	 * if so updates the action as UPDATE 
	 * & if new one is added, changes the action as CREATED_NEW
	 * @param field
	 * @param clazz
	 * @param original
	 * @param updated
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	private static void handleCollectionObjects(Field field, Class clazz, 
			Entity original, Entity updated) 
			throws IllegalArgumentException, IllegalAccessException {
		if (Collection.class.isAssignableFrom(clazz)) {
			ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
			Type[] genericTypes = parameterizedType.getActualTypeArguments();
			for (Type generaticType : genericTypes) {
				Class genericParameterClass = (Class) generaticType;						
				if( Entity.class.isAssignableFrom( genericParameterClass ) ) {
					List<Entity> orginalChildEntities = (List<Entity>) field.get(original);
					List<Entity> updatedChildEntities = (List<Entity>) field.get(updated);
					
					if (orginalChildEntities != null && 
							updatedChildEntities != null) {
						TreeMap<Entity, Entity> originalChildMap = new TreeMap<Entity, Entity>(entityIdComparator);
						for (Entity entity : orginalChildEntities) {
							originalChildMap.put(entity, entity);
						}
						
						for (Entity updatedChild : updatedChildEntities) {
							if (originalChildMap.containsKey(updatedChild)) {
								// existing one
								Entity originalChild = originalChildMap.get(updatedChild);
								setCreateOrUpdateAction(originalChild, updatedChild);
							} else {
								// newly added ones
								Long updatedChildId = updatedChild.getId();
								if (updatedChildId == null) {
									Action action = new Action();
									updatedChild.setAction(action);
								}
							}
						}
					}
				}
			}
		}
	}
	
	private static Comparator<Entity> entityIdComparator = new Comparator<Entity>() {
		public int compare(Entity o1, Entity o2) {
			Long id1= o1.getId();
			Long id2= o2.getId();
			if (id1 == null || id2 == null) {
				return id1 == id2 ? 0 : -1;
			}
			return id1.intValue() - id2.intValue();
		}
	};

	public static String getTempDirectory() {
		String tmpdir = System.getProperty("java.io.tmpdir");
		if (tmpdir == null) {
			tmpdir = File.separator;
		}
		if (!tmpdir.endsWith(File.separator)) {
			tmpdir += File.separator;
		}
		return tmpdir;
	}
}
