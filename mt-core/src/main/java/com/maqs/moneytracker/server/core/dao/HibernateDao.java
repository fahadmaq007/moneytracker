package com.maqs.moneytracker.server.core.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.list.PageableList;
import com.maqs.moneytracker.common.paging.spec.Operation;
import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.transferobjects.Action;
import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.util.Util;
import com.maqs.moneytracker.server.core.dao.querytranslator.QueryTranslator;
import com.maqs.moneytracker.server.core.dao.querytranslator.RestrictionTranslator;
import com.maqs.moneytracker.server.core.exception.DataAccessException;

/**
 * Implementation of the {@link IDao}.
 * It provides the implementation to
 * the generic DAO operations.
 * 
 * @author maqbool.ahmed
 * 
 */
public class HibernateDao implements IDao {

	protected Logger logger = Logger.getLogger(getClass());

	protected Hibernate4DaoSupport daoSupport;
	
	public HibernateDao() {
		
	}

	protected Hibernate4DaoSupport getHibernateTemplate() {
		return getDaoSupport();
	}
	
	public void setDaoSupport(Hibernate4DaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	
	public Hibernate4DaoSupport getDaoSupport() {
		return daoSupport;
	}
	/**
	 * {@inheritDoc}
	 */
	public Entity save(Entity e) throws DataAccessException {
		try {
			e.setId(null); // explicitly make the id as null as this operation
							// always makes a new insert.
			if (Action.CREATE_NEW == e.getAction().getActionIndex()) {
				Long id = (Long) getHibernateTemplate().save(e);
				e.setId(id);
				logger.info(e.getClass().getSimpleName()
						+ " is successfully created with the id [" + id + "].");
			} else {
				logger.warn("expected entity action " + Action.CREATE_NEW
						+ ", but actual is " + e.getAction().getActionIndex());
			}
		} catch (Exception ex) {
			throw new DataAccessException(ex);
		}
		Action.setDoNothingAction(e);
		return e;
	}

	/**
	 * {@inheritDoc}
	 */
	public void update(Entity e) throws DataAccessException {
		try {
			Long id = e.getId();
			if (Action.UPDATE == e.getAction().getActionIndex()) {
				if (id == null) {
					throw new DataAccessException("id is null, the entity cannot be updated...");
				}
				getHibernateTemplate().update(e);
				if (logger.isInfoEnabled()) {
					logger.info(e.getClass().getSimpleName() + ":" + id
							+ " is successfully updated.");
				}
			} else {
				logger.warn("expected entity action " + Action.UPDATE
						+ ", but actual is " + e.getAction().getActionIndex());
			}
		} catch (Exception ex) {
			throw new DataAccessException(ex);
		}
		Action.setDoNothingAction(e);
	}

	/**
	 * {@inheritDoc}
	 */
	public Entity saveOrUpdate(Entity e) throws DataAccessException {
		try {
			if (Action.CREATE_NEW == e.getAction().getActionIndex()) {
				save(e);
			} else if (Action.UPDATE == e.getAction().getActionIndex()) {
				update(e);
			}
		} catch (Exception ex) {
			throw new DataAccessException(ex);
		}
		return e;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<? extends Entity> saveAll(List<? extends Entity> list)
			throws DataAccessException {
		try {
			List<Entity> newEntities = new ArrayList<Entity>();
			List<Entity> updatedEntities = new ArrayList<Entity>();
			List<Entity> saveOrUpdateList = new ArrayList<Entity>();
			List<Entity> doNothingEntities = new ArrayList<Entity>();
			for (Entity entity : list) {
				if (Action.CREATE_NEW == entity.getAction().getActionIndex()) {
					newEntities.add(entity);
				} else if (Action.UPDATE == entity.getAction().getActionIndex()) {
					if (entity.getId() == null) {
						throw new DataAccessException("id is null, the entity cannot be updated...");
					}
					updatedEntities.add(entity);
				} else if (Action.DO_NOTHING == entity.getAction().getActionIndex()) {
					doNothingEntities.add(entity);
				}
			}
			if (CollectionsUtil.isNonEmpty(doNothingEntities)) {
				logger.warn("the following list of entities are DO_NOTHING, hence they cannot be stored."
						+ doNothingEntities);
			}
			setNullToEntityId(newEntities);
			saveOrUpdateList.addAll(newEntities);
			saveOrUpdateList.addAll(updatedEntities);
			getHibernateTemplate().saveOrUpdateAll(saveOrUpdateList);
		} catch (Exception ex) {
			throw new DataAccessException(ex);
		}
		Action.setDoNothingAction(list);
		return list;
	}

	/**
	 * {@inheritDoc}
	 */
	public Entity getEntity(Class<? extends Entity> clazz, Long id)
			throws DataAccessException {
		try {
			Entity e = getHibernateTemplate().get(clazz, id);
			Action.setDoNothingAction(e);
			return e;
		} catch (Exception ex) {
			throw new DataAccessException(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Entity getEntity(Class<? extends Entity> clazz, String propertyName,
			Object value) throws DataAccessException {
		QuerySpec querySpec = createQuerySpec(clazz);
		addPropertySpec(querySpec, propertyName, Operation.EQ, value);
		Page page = new Page(1); // 1 - to fetch only one record
		List list = listAll(querySpec, page);
		Entity e = (Entity) (list != null && list.size() > 0 ? list.get(0) : null); 
		Action.setDoNothingAction(e);
		return e;
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeEntity(Class<? extends Entity> clazz, Long id)
			throws DataAccessException {
		try {
			Entity entity = getHibernateTemplate().get(clazz, id);

			if (entity != null)
				getHibernateTemplate().delete(entity);
		} catch (Exception ex) {
			throw new DataAccessException(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void removeAll(List<? extends Entity> entities)
			throws DataAccessException {
		try {
			if (CollectionsUtil.isNonEmpty(entities)) {
				getHibernateTemplate().deleteAll(entities);
			}
		} catch (Exception ex) {
			throw new DataAccessException(ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public long count(QuerySpec spec) throws DataAccessException {
		final String countQuery = QueryTranslator.createCountQuery(spec);
		Session session = getHibernateTemplate().getCurrentSession();
		Query hibQuery = session.createQuery(countQuery);
		QueryTranslator.setParams(hibQuery,
				QueryTranslator.getValues(spec));
		Long count = (Long) hibQuery.uniqueResult();
		if (logger.isDebugEnabled()) {
			logger.debug("count() on " + spec.getClassName() + " produced "
					+ count + " records.");
		}

		return (count == null ? 0 : count);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<? extends Entity> listAll(QuerySpec spec, Page page)
			throws DataAccessException {
		String classname = (spec == null ? "noQuerySpec" : spec.getClassName());
		logger.debug("listing all entities of type " + classname);
		List<? extends Entity> list = null;
		try {
			DetachedCriteria c = createDetachedCriteria(spec.getClassName());
			if (spec != null) {
				translateCriteria(c, spec);
			}
			if (page != null) {
				int pageIndex = page.getPageNumber();
				int pageSize = page.getPageSize();
				int firstResult = (pageIndex - 1) * pageSize;
				long count = count(spec);
				if (count > 0) {
					page.setTotalRecords(count);
					List<? extends Entity> l = getHibernateTemplate().findByCriteria(c,
							firstResult, pageSize);
					list = new PageableList(l, page);
				}
			} else {
				logger.warn("page is null, please use paging feature to reduce the burden on db.");
				list = getHibernateTemplate().findByCriteria(c);
			}
		} catch (DataAccessException e) {
			throw e;
		} catch (Exception e) {
			throw new DataAccessException(e);
		}

		Action.setDoNothingAction(list);
		logger.info("listAll produced " + (list == null ? 0 : list.size())
				+ " records.");
		return (list == null ? new ArrayList() : list);
	}
	
	@Override
	public Entity getEntity(QuerySpec spec)
			throws DataAccessException {
		List<? extends Entity> list= listAll(spec, null);
		Entity e= (Entity) (list != null && list.size() > 0 ? list.get(0) : null); 
		return e;
	}


	/**
	 * {@inheritDoc}
	 */
	public List<? extends Entity> listAll(QuerySpec spec)
			throws DataAccessException {
		return listAll(spec, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public List<? extends Entity> listAll(Class<? extends Entity> clazz,
			String propertyName, Long parentId) throws DataAccessException {
		QuerySpec querySpec = createQuerySpec(clazz);
		addPropertySpec(querySpec, propertyName, Operation.EQ, parentId);
		return listAll(querySpec, null);
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<Long, ? extends Entity> listByIds(Class<? extends Entity> clazz,
			String propertyName, Collection idList) throws DataAccessException {
		QuerySpec querySpec = createQuerySpec(clazz);
		addPropertySpec(querySpec, propertyName, Operation.IN, idList);
		List<? extends Entity> entities = listAll(querySpec, null);
		if (CollectionsUtil.isNonEmpty(entities)) {
			int size = entities.size();
			Map<Long, Entity> map = new HashMap<Long, Entity>(size);
			for (Entity entity : entities) {
				map.put(entity.getId(), entity);
			}
			return map;
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public Map<Long, List<? extends Entity>> listChildrenByParentIds(
			Class<? extends Entity> clazz, String propertyName,
			Collection idList) throws DataAccessException {
		QuerySpec querySpec = createQuerySpec(clazz);
		addPropertySpec(querySpec, propertyName, Operation.IN, idList);
		List<? extends Entity> entities = listAll(querySpec, null);
		if (CollectionsUtil.isNonEmpty(entities)) {
			int size = entities.size();
			Map<Long, List<? extends Entity>> map = new HashMap<Long, List<? extends Entity>>(
					size);
			for (Entity entity : entities) {
				Long parentId = getParentId(propertyName, entity);
				List<Entity> childList = null;
				if (map.containsKey(parentId)) {
					childList = (List<Entity>) map.get(parentId);
				} else {
					childList = new ArrayList<Entity>();
				}
				childList.add(entity);
				map.put(parentId, childList);
			}
			return map;
		}
		return null;
	}

	/**
	 * Gets the parent id.
	 * @param propertyName
	 * @param entity
	 * @return
	 * @throws DataAccessException
	 */
	private Long getParentId(String propertyName, Entity entity)
			throws DataAccessException {
		if (entity != null) {
			Class entityClass = entity.getClass();
			String methodName = "get" + StringUtils.capitalize(propertyName);
			try {
				Method m = entityClass.getDeclaredMethod(methodName,
						(Class[]) null);
				return (Long) m.invoke(entity, (Object[]) null);
			} catch (Exception e) {
				throw new DataAccessException(e.getMessage(), e);
			}
		}
		return null;
	}

	/**
	 * Creates a {@link DetachedCriteria}
	 * 
	 * @param className
	 * @return
	 * @throws DataAccessException
	 */
	protected DetachedCriteria createDetachedCriteria(String className)
			throws DataAccessException {
		try {
			Class clazz = getClazz(className);
			return DetachedCriteria.forClass(clazz);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Translates the QuerySpec & adds the restrictions to the criteria.
	 * 
	 * @param c
	 *            criteria
	 * @param querySpec
	 *            spec
	 * @return criteria with restrictions
	 */
	protected DetachedCriteria translateCriteria(DetachedCriteria c,
			QuerySpec querySpec) throws DataAccessException {
		if (querySpec != null) {
			RestrictionTranslator translator = new RestrictionTranslator();
			translator.translate(c, querySpec);
		}
		return c;
	}

	/**
	 * Gets the Class instance
	 * 
	 * @param clazz
	 *            class to be created
	 * @return Class
	 * @throws Exception
	 *             In case it fails to find the clazz
	 */
	private Class<? extends Entity> getClazz(String clazz)
			throws DataAccessException {
		try {
			return (Class<? extends Entity>) Class.forName(clazz);
		} catch (ClassNotFoundException e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * Creates the clone of a DetachedCriteria
	 * 
	 * @param source
	 *            source
	 * @return clone object
	 */
	protected DetachedCriteria getClone(DetachedCriteria source) {
		DetachedCriteria clone = DetachedCriteria.forClass(source.getClass());
		return (DetachedCriteria) Util.copyProperties(clone, source);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsById(Class<? extends Entity> clazz, Long id)
			throws DataAccessException {
		return containsByProperty(clazz, "id", id);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean containsByProperty(Class<? extends Entity> clazz,
			String propertyName, Object value) throws DataAccessException {
		if (clazz == null || value == null)
			return false;

		QuerySpec querySpec = createQuerySpec(clazz);
		addPropertySpec(querySpec, propertyName, Operation.EQ, value);
		return contains(querySpec);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean contains(QuerySpec querySpec) throws DataAccessException {
		return count(querySpec) > 0;
	}

	/**
	 * Creates QuerySpec for the given class.
	 * 
	 * @param clazz
	 *            class
	 * @return QuerySpec
	 */
	protected QuerySpec createQuerySpec(Class<? extends Entity> clazz) {
		return new QuerySpec(clazz.getName());
	}

	/**
	 * Adds the new PropertySPec to the querySpec.
	 * 
	 * @param querySpec
	 *            querySpec
	 * @param propertyName
	 *            property name
	 * @param operation
	 *            opeartion
	 * @param value
	 *            value
	 */
	protected void addPropertySpec(QuerySpec querySpec, String propertyName,
			Operation operation, Object value) {
		PropertySpec propertySpec = new PropertySpec(propertyName, operation,
				value);
		querySpec.addPropertySpec(propertySpec);
	}

	/**
	 * Loops though the collection & makes id=null
	 * 
	 * @param entities
	 */
	protected void setNullToEntityId(Collection<? extends Entity> entities) {
		for (Entity entity : entities) {
			entity.setId(null);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public int removeAll(Class<? extends Entity> clazz, String propertyName,
			Object value) throws DataAccessException {
		return removeAll(clazz, propertyName, Operation.EQ, value);
	}

	/**
	 * {@inheritDoc}
	 */
	public int removeAll(Class<? extends Entity> clazz, String propertyName,
			Operation operation, Object value) throws DataAccessException {
		QuerySpec querySpec = createQuerySpec(clazz);
		addPropertySpec(querySpec, propertyName, operation, value);
		return removeAll(querySpec);
	}

	/**
	 * {@inheritDoc}
	 */
	public int removeAll(QuerySpec querySpec) throws DataAccessException {
		if (querySpec == null) {
			throw new DataAccessException("spec is null");
		}
		String classname = (querySpec == null ? null : querySpec.getClassName());
		logger.debug("deleting all entities of type " + classname);
		String query = QueryTranslator.createDeleteQuery(querySpec);
		try {
			return getHibernateTemplate().bulkUpdate(query,
					QueryTranslator.getValues(querySpec));
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public List<? extends Entity> listSelectiveProperties(QuerySpec querySpec,
			Page page) throws DataAccessException {
		final String query = QueryTranslator
				.createSelectivePropertiesQuery(querySpec);
		logger.debug("Translated Query: " + query);
		Session session = getHibernateTemplate().getCurrentSession();
		Query hibQuery = session.createQuery(query);
		
		if (page != null) {
			int pageIndex = page.getPageNumber();
			int pageSize = page.getPageSize();
			int firstResult = (pageIndex - 1) * pageSize;
			long count = count(querySpec);
			page.setTotalRecords(count);
			hibQuery.setFetchSize(pageSize);
			hibQuery.setFirstResult(firstResult);
		}
		QueryTranslator.setParams(hibQuery,
				QueryTranslator.getValues(querySpec));
		List<? extends Entity> results = hibQuery.list();
		Action.setDoNothingAction(results);
		return results;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<? extends Entity> listEntitiesByNamedQuery(String namedQueryId, 
			Object[] paramValues) throws DataAccessException {
		List<? extends Entity> entities = null;
		
		try {
			entities = getHibernateTemplate().findByNamedQuery(namedQueryId, paramValues);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
		
		return entities;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object uniqueResultByNamedQuery(final String namedQueryId, final Object[] paramValues) throws DataAccessException {
		Object o = null;
		try {
			o = getHibernateTemplate().executeUnique(namedQueryId, paramValues);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
		return o;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List executeNamedQuery(final String namedQueryId, 
			final Object[] paramValues) throws DataAccessException {
		List resultset = null;
		try {
			resultset = getHibernateTemplate().executeNamedQuery(namedQueryId, paramValues);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
		return resultset;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List executeNamedSQLQuery(final String namedQueryId, 
			final Object[] paramValues) throws DataAccessException {
		return executeNamedSQLQuery(namedQueryId, paramValues, null);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List executeNamedSQLQuery(final String namedQueryId, 
			final Object[] paramValues, 
			final Class transformClass) throws DataAccessException {
		List resultset = null;
		try {
			resultset = getHibernateTemplate().executeNamedSQLQuery(namedQueryId, 
					paramValues, transformClass);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
		return resultset;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void updateNamedSQLQuery(final String namedQueryId, 
			final Object[] paramValues) throws DataAccessException {
		try {
			getHibernateTemplate().executeUpdate(namedQueryId, paramValues);
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}

	@Override
	public List executeNamedQuery(final String namedQueryId,
			QuerySpec spec) throws DataAccessException {
		try {
			Query q = getHibernateTemplate().getCurrentSession().getNamedQuery(namedQueryId);
			
			return q.list();
		} catch (Exception e) {
			throw new DataAccessException(e);
		}
	}
}
