package com.maqs.moneytracker.server.core.dao;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.transform.Transformers;

import com.maqs.moneytracker.common.transferobjects.Entity;

/**
 * Wrappers around new hibernate functionality.  Allows most spring 2 
 * style dao's to operate unchanged after being 
 * upgraded to Spring 3.1 and Hibernate 4.
 * @author Sam Thomas
 *
 */

public class Hibernate4DaoSupport {
	
	private SessionFactory sessionFactory;
	
	public Hibernate4DaoSupport() {
		
	}
	
	protected Session getCurrentSession() {
		Session session = getSessionFactory().getCurrentSession();
		return session;
	}
	
	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory)  {
		this.sessionFactory = sessionFactory;
	}
	
	protected Hibernate4DaoSupport getHibernateTemplate() {
		return this;
	}
	
	public void delete(Entity obj) throws Exception {
		getCurrentSession().delete(obj);
	}

	public void deleteAll(Collection<? extends Entity> items) throws Exception {
		for(Entity item : items) {
			delete(item);
		}
	}
	
	public void saveOrUpdate(Entity obj) throws Exception {
		getCurrentSession().saveOrUpdate(obj);
	}
	
	public void update(Object obj) throws Exception {
		getCurrentSession().update(obj);
	}
	
	public void saveOrUpdateAll(Collection<? extends Entity> items) throws Exception {
		for(Entity item : items) {
			saveOrUpdate(item);
		}
	}
	
	public Serializable save(Entity obj) throws Exception {
		return getCurrentSession().save(obj);
	}

	public void saveAll(Collection<? extends Entity> items) throws Exception {
		for(Entity item : items) {
			save(item);
		}
	}
	
	
	/**
	 * Simulate spring 2's "getHibernateTemplate().get()" method.
	 */
	protected Entity get(Class<? extends Entity> clazz, Long id) throws Exception {
		Entity result = (Entity) getCurrentSession().get(clazz, id);
		return result;
	}

	
	protected List<? extends Entity> find(String query) throws Exception {
		return getCurrentSession().createQuery(query).list();
	}
	
	
	protected List<? extends Entity> find(String query, List<Object> args) throws Exception {
		Query q = getCurrentSession().createQuery(query);

    	for(int i=0; i<args.size(); i++) {
    		q.setParameter(i, args.get(i));
    	}
    	
    	return q.list();
	}
	
	
	protected List<? extends Entity> find(String query, Object ...args) throws Exception {
		Query q = getCurrentSession().createQuery(query);

    	for(int i=0; i<args.length; i++) {
    		q.setParameter(i, args[i]);
    	}
    	
    	return q.list();
	}
	
	protected List<? extends Entity> findByCriteria(DetachedCriteria criteria) throws Exception {
		return criteria.getExecutableCriteria(getCurrentSession()).list();
	}

	
	protected List<? extends Entity> 
		findByCriteria(DetachedCriteria criteria, int firstResult, int maxResults) throws Exception {
		return criteria.getExecutableCriteria(getCurrentSession())
				.setFirstResult(firstResult)
				.setMaxResults(maxResults)
				.list();
	}
	
	public Object executeUnique(final String namedQueryId, final Object[] paramValues) throws Exception {
		Query query = getCurrentSession().getNamedQuery(namedQueryId);
		setParams(query, paramValues);
		return query.uniqueResult();
	}
	
	protected void setParams(Query query, Object[] paramValues) {
		if (paramValues != null && paramValues.length > 0) {
			int i = 0;
			for (Object param : paramValues) {
				if (param instanceof Collection) {
					query.setParameterList("list", (Collection) param);
				} else if (param instanceof Object[]) {
					query.setParameterList("list", (Object[]) param);
				} else {
					query.setParameter(i++, param);
				}
			}
		}
	}

	public List executeNamedQuery(final String namedQueryId, final Object[] paramValues) throws Exception {
		Query query = getCurrentSession().getNamedQuery(namedQueryId);
		setParams(query, paramValues);
		return query.list();
	}

	public int bulkUpdate(String sql, Object[] paramValues) {
		Query query = getCurrentSession().createQuery(sql);
		setParams(query, paramValues);
		int update = query.executeUpdate();
		return update;
	}

	public List<? extends Entity> findByNamedQuery(String namedQueryId,
			Object[] paramValues) throws Exception {
		Query query = getCurrentSession().getNamedQuery(namedQueryId);
		setParams(query, paramValues);
		return query.list();
	}

	public int executeUpdate(String namedQueryId, Object[] paramValues) throws Exception {
		Query query = getCurrentSession().getNamedQuery(namedQueryId);
		setParams(query, paramValues);
		return query.executeUpdate();
	}

	public List executeNamedSQLQuery(String namedQueryId, Object[] paramValues,
			Class transformClass) throws Exception {
		SQLQuery query = (SQLQuery) getCurrentSession().getNamedQuery(namedQueryId);
		setParams(query, paramValues);
		if (transformClass != null) {
			query.setResultTransformer(Transformers.aliasToBean(transformClass)); 
		}
		return query.list();
	}
	
}
