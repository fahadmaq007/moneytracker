package com.maqs.moneytracker.server.core.dao.querytranslator;

import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import com.maqs.moneytracker.common.paging.spec.Operation;
import com.maqs.moneytracker.common.paging.spec.OrderByOperation;
import com.maqs.moneytracker.common.paging.spec.OrderBySpec;
import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.util.StringUtil;
import com.maqs.moneytracker.server.core.exception.DataAccessException;

/**
 * This class translates the QuerySpec to Restriction & add the same on the
 * criteria.
 * 
 * @author maqbool.ahmed
 * 
 */
@SuppressWarnings("deprecation")
public class RestrictionTranslator {

	private Logger logger = Logger.getLogger(getClass());

	/**
	 * Translates the query spec & adds the restriction to the
	 * {@link DetachedCriteria}
	 * 
	 * @param c
	 *            Criteria to add the restriction
	 * @param querySpec
	 *            QuerySpec to be translated
	 * @return {@link DetachedCriteria}
	 */
	public DetachedCriteria translate(DetachedCriteria c, QuerySpec querySpec)
			throws DataAccessException {
		List<PropertySpec> propertySpecs = querySpec.getPropertySpecs();
		if (CollectionsUtil.isNonEmpty(propertySpecs)) {
			for (PropertySpec propertySpec : propertySpecs) {
				addRestriction(c, propertySpec);
			}
		}
		
		List<OrderBySpec> orderSpecs = querySpec.getOrderBySpecs();
		if (CollectionsUtil.isNonEmpty(orderSpecs)) {
			for (OrderBySpec orderSpec : orderSpecs) {
				addOrderBy(c, orderSpec);
			}
		}
		return c;
	}

	/**
	 * Adds the Order-By-Clause as per the given {@link OrderBySpec}
	 * 
	 * @param c
	 *            - generated criteria
	 * @param orderBySpec
	 *            - given {@link OrderBySpec}
	 */
	protected void addOrderBy(DetachedCriteria c, OrderBySpec orderBySpec) {
		Order order = null;
		String propName = orderBySpec.getPropertyName();
		OrderByOperation operation = orderBySpec.getOperation();
		if (operation != null & OrderByOperation.ASC == operation) {
			order = Order.asc(propName);
		} else {
			order = Order.desc(propName);
		}
		c.addOrder(order);
	}

	/**
	 * Adds the restriction as per the {@link PropertySpec}
	 * 
	 * @param c
	 *            {@link DetachedCriteria}
	 * @param propertySpec
	 *            {@link PropertySpec} to be translated
	 */
	protected void addCriterion(DetachedCriteria c, PropertySpec propertySpec)
			throws DataAccessException {
		Operation op = propertySpec.getOperation();
		String propertyName = propertySpec.getPropertyName();
		Object value = propertySpec.getValue();
		Criterion criterion = null;
		if (op == null || StringUtil.nullOrEmpty(propertyName)) {
			String msg = "invalid propertySpec " + propertySpec;
			logger.error(msg);
			throw new DataAccessException(msg);
		}

		switch (op) {
		case EQ:
			criterion = Property.forName(propertyName).eq(value);
			break;

		case LIKE:
			criterion = Property.forName(propertyName).like(value);
			break;

		case GT:
			criterion = Property.forName(propertyName).gt(value);
			break;

		case LT:
			criterion = Property.forName(propertyName).lt(value);
			break;

		case BETWEEN:
			criterion = createBetweenCriterion(propertyName, value);
			break;

		case GTE:
			criterion = Property.forName(propertyName).ge(value);
			break;

		case LTE:
			criterion = Property.forName(propertyName).le(value);
			break;

		case NOTEQUAL:
			criterion = Property.forName(propertyName).ne(value);
			break;

		case ISNULL:
			criterion = Property.forName(propertyName).isNull();
			break;
		case IS_NOT_NULL:
			criterion = Property.forName(propertyName).isNotNull();
			break;
		case IN:
		case NOT_IN:
			if (value == null)
				break;
			if (value instanceof Collection) {
				Collection list = (Collection) value;
				if (CollectionsUtil.isNonEmpty(list)) {
					if (op == Operation.IN) {
						criterion = Property.forName(propertyName).in(list);
					} else {
						criterion = Restrictions.not(Restrictions.in(
								propertyName, list));
					}
				}
			}
			break;

		default:
			throw new IllegalArgumentException("invalid operation " + op);
		}

		if (criterion != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("adding the criterion to the DetachedCriteria based on propertySpec "
						+ propertySpec);
			}
			c.add(criterion);
		}
	}

	/**
	 * Adds the expression as per the {@link PropertySpec}
	 * 
	 * @param c
	 *            {@link DetachedCriteria}
	 * @param propertySpec
	 *            {@link PropertySpec} to be translated
	 */
	protected void addExpression(DetachedCriteria c, PropertySpec propertySpec)
			throws DataAccessException {
		Operation op = propertySpec.getOperation();
		String propertyName = propertySpec.getPropertyName();
		Object value = propertySpec.getValue();
		if (op == null || StringUtil.nullOrEmpty(propertyName)) {
			String msg = "invalid propertySpec " + propertySpec;
			logger.error(msg);
			throw new DataAccessException(msg);
		}

		switch (op) {
		case EQ:
			c.add(Expression.eq(propertyName, value));
			break;

		case LIKE:
			c.add(Expression.like(propertyName, value));
			break;

		case GT:
			c.add(Expression.gt(propertyName, value));
			break;

		case LT:
			c.add(Expression.lt(propertyName, value));
			break;

		case BETWEEN:
			if (value instanceof Object[]) {
				Object[] values = (Object[]) value;
				if (values.length >= 2)
					c.add(Expression
							.between(propertyName, values[0], values[1]));
			}
			break;

		case GTE:
			c.add(Expression.ge(propertyName, value));
			break;

		case LTE:
			c.add(Expression.le(propertyName, value));
			break;

		case NOTEQUAL:
			c.add(Expression.ne(propertyName, value));
			break;

		case ISNULL:
			c.add(Expression.isNull(propertyName));
			break;
		case IS_NOT_NULL:
			c.add(Expression.isNotNull(propertyName));
			break;
		case IN:
		case NOT_IN:
			if (value == null)
				break;
			if (value instanceof Collection) {
				Collection list = (Collection) value;
				if (CollectionsUtil.isNonEmpty(list)) {
					if (op == Operation.IN) {
						c.add(Expression.in(propertyName, list));
					} else {
						c.add(Expression.not(Expression.in(propertyName, list)));
					}
				}
			}
			break;

		default:
			throw new IllegalArgumentException("invalid operation " + op);
		}

	}

	/**
	 * Adds the restriction as per the {@link PropertySpec}
	 * 
	 * @param c
	 *            {@link DetachedCriteria}
	 * @param propertySpec
	 *            {@link PropertySpec} to be translated
	 */
	protected void addRestriction(DetachedCriteria c, PropertySpec propertySpec)
			throws DataAccessException {
		Operation op = propertySpec.getOperation();
		String propertyName = propertySpec.getPropertyName();
		Object value = propertySpec.getValue();
		if (op == null || StringUtil.nullOrEmpty(propertyName)) {
			String msg = "invalid propertySpec " + propertySpec;
			logger.error(msg);
			throw new DataAccessException(msg);
		}

		switch (op) {
		case EQ:
			c.add(Restrictions.eq(propertyName, value));
			break;

		case LIKE:
			c.add(Restrictions.like(propertyName, value));
			break;

		case GT:
			c.add(Restrictions.gt(propertyName, value));
			break;

		case LT:
			c.add(Restrictions.lt(propertyName, value));
			break;

		case BETWEEN:
			if (value instanceof Object[]) {
				Object[] values = (Object[]) value;
				if (values.length >= 2)
					c.add(Restrictions.between(propertyName, values[0],
							values[1]));
			}
			break;

		case GTE:
			c.add(Restrictions.ge(propertyName, value));
			break;

		case LTE:
			c.add(Restrictions.le(propertyName, value));
			break;

		case NOTEQUAL:
			c.add(Restrictions.ne(propertyName, value));
			break;

		case ISNULL:
			c.add(Restrictions.isNull(propertyName));
			break;
		case IS_NOT_NULL:
			c.add(Restrictions.isNotNull(propertyName));
			break;
		case IN:
		case NOT_IN:
			if (value == null)
				break;
			if (value instanceof Collection) {
				Collection list = (Collection) value;
				if (CollectionsUtil.isNonEmpty(list)) {
					if (op == Operation.IN) {
						c.add(Restrictions.in(propertyName, list));
					} else {
						c.add(Restrictions.not(Restrictions.in(propertyName,
								list)));
					}
				}
			} else if (value instanceof Object[]) {
				Object[] array = (Object[]) value;
				if (array != null && array.length > 0) {
					if (op == Operation.IN) {
						c.add(Restrictions.in(propertyName, array));
					} else {
						c.add(Restrictions.not(Restrictions.in(propertyName,
								array)));
					}
				}
			}
			break;

		default:
			throw new IllegalArgumentException("invalid operation " + op);
		}

	}

	/**
	 * Creates the range criterion (especially for dates)
	 * 
	 * @param propertyName
	 * @param value
	 * @return
	 */
	private Criterion createBetweenCriterion(String propertyName, Object value) {
		if (value instanceof Object[]) {
			Object[] array = (Object[]) value;
			if (array == null || array.length == 0)
				return null;

			Object from = null;
			Object to = null;
			if (array.length <= 1) {
				from = array[0];
				to = array[0];
			} else {
				from = array[0];
				to = array[1];
			}
			return Property.forName(propertyName).between(from, to);
		} else if (value != null) {
			return Property.forName(propertyName).between(value, value);
		}
		return null;
	}

}
