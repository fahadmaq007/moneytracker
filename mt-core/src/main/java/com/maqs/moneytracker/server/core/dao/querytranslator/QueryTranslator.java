package com.maqs.moneytracker.server.core.dao.querytranslator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;

import com.maqs.moneytracker.common.paging.spec.Operation;
import com.maqs.moneytracker.common.paging.spec.PropertySpec;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.paging.spec.SelectSpec;
import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.common.util.CollectionsUtil;
import com.maqs.moneytracker.common.util.StringUtil;
import com.maqs.moneytracker.server.core.exception.DataAccessException;

/**
 * QueryTranslator is responsible for creating queries out of {@link QuerySpec}.
 * 
 * @author maqbool.ahmed
 *
 */
public class QueryTranslator {

	private static Logger logger = Logger.getLogger(QueryTranslator.class);

	private static final String DELETE_CLAUSE = "delete ";
	
	private static final String COUNT_CLAUSE = "count(*) ";
	
	private static final String AND_CLAUSE = " and ";
	
	private static final String FROM_CLAUSE = "from ";
	
	private static final String NEW_CLAUSE = "new ";
	
	private static final String WHERE_CLAUSE = " where ";
	
	private static final String EQUALS = " = ";
	
	private static final String QUESTION_MARK = "?";

	private static final String LIKE_TEXT = " like ";

	private static final String GT_TEXT = ">";

	private static final String LT_TEXT = "<";

	private static final String BETWEEN_TEXT = " between ";

	private static final String GTE_TEXT = ">=";

	private static final String LTE_TEXT = "<=";

	private static final String NOTEQ_TEXT = "!=";

	private static final String ISNULL_TEXT = " is null ";
	
	private static final String IS_NOT_NULL_TEXT = " is not null ";

	private static final String IN_TEXT = " in ";

	private static final String NOTIN_TEXT = " not in ";
	
	private static final String COMMA = ",";

	private static final Object SPACE = " ";
	
	private static final String OPEN_PARANTHESIS = "(";
	
	private static final String CLOSE_PARANTHESIS = ")";

	private static final String SELECT_CLAUSE = "select ";

	private static final String PERC = "%";

	private static final String QUOTE = "'";
	
	private QueryTranslator() {
		
	}
	/**
	 * SELECT count(distinct E.firstName) FROM Employee E
	 * @param spec
	 * @return
	 */
	public static String createCountQuery(QuerySpec spec) throws DataAccessException {
		Class clazz = getClass(spec);
		StringBuilder countQuery = new StringBuilder(SELECT_CLAUSE);
		countQuery.append(COUNT_CLAUSE);
		countQuery.append(FROM_CLAUSE).append(clazz.getSimpleName());
		countQuery.append(createWhereClause(spec.getPropertySpecs()));
		String query = countQuery.toString();
		logger.debug("generated query: " + query);
		return query;
	}
	/**
	 * Creates a delete SQL query from the QuerySpec
	 * @param spec
	 * @return query string
	 * @throws DataAccessException
	 */
	public static String createDeleteQuery(QuerySpec spec) throws DataAccessException {
		StringBuilder deleteQuery = new StringBuilder(DELETE_CLAUSE);
		deleteQuery.append(createQuery(spec));
		String query = deleteQuery.toString();
		logger.debug("generated query: " + query);
		return query;
	}
	
	/**
	 * Creates a select SQL query from the QuerySpec
	 * For Eg. select new Employee(id, name) from Employee where id = ?
	 * @param spec
	 * @return query string
	 * @throws DataAccessException
	 */
	public static String createSelectivePropertiesQuery(QuerySpec spec) throws DataAccessException {
		Class clazz = getClass(spec);
		StringBuilder selectQuery = new StringBuilder(SELECT_CLAUSE);
		List<SelectSpec> selectSpecs = spec.getSelectSpecs();
		if (selectSpecs == null || selectSpecs.size() ==0) {
			throw new DataAccessException("Query with selective properties should have selectSpecs in the QuerySpec");
		}
		selectQuery.append(buildSelectivePropertiesQuery(clazz, selectSpecs));
		List<PropertySpec> propSpecs = spec.getPropertySpecs();
		selectQuery.append(createWhereClause(propSpecs));
		String query = selectQuery.toString();
		logger.debug("generated query: " + query);
		return query;
	}
	
	/**
	 * 
	 * @param clazz
	 * @param selectSpecs
	 * @return
	 */
	private static StringBuilder buildSelectivePropertiesQuery(
			Class<? extends Entity> clazz, List<SelectSpec> selectSpecs) {
		StringBuilder query = new StringBuilder();
		query.append(NEW_CLAUSE).append(clazz.getSimpleName()).append(OPEN_PARANTHESIS);
		for (int i = 0, n = selectSpecs.size(); i < n; i++) {
			SelectSpec selectSpec = selectSpecs.get(i);
			query.append(selectSpec.getPropertyName());
			if (i < n - 1) { 
				query.append(COMMA);
			}	
		}
		query.append(CLOSE_PARANTHESIS);
		query.append(SPACE);
		query.append(FROM_CLAUSE).append(clazz.getSimpleName());
		
		return query;
	}
	
	/**
	 * Creates the query string from the QuerySpec
	 * @param spec
	 * @return query string
	 * @throws DataAccessException
	 */
	public static String createQuery(QuerySpec spec) throws DataAccessException {
		Class clazz = getClass(spec);
		StringBuilder selectQuery = new StringBuilder(FROM_CLAUSE);
		selectQuery.append(clazz.getSimpleName());
		List<PropertySpec> propSpecs = spec.getPropertySpecs();
		selectQuery.append(createWhereClause(propSpecs));	
		String query = selectQuery.toString();
		logger.debug("generated query: " + query);
		return query;
	}
	
	/**
	 * Retrieves the property values from the List<PropertySpec>
	 * and puts in an Object[].
	 * @param spec querySpec 
	 * @return Object[] of property values
	 */
	public static Object[] getValues(QuerySpec spec) {
		List list = new ArrayList();
		List<PropertySpec> propSpecs = spec.getPropertySpecs();
		if (CollectionsUtil.isNonEmpty(propSpecs)) {
			int n = propSpecs.size();
			for (int i = 0; i < n; i++) {
				PropertySpec propSpec = propSpecs.get(i); 
				List valueList = getValues0(propSpec);
				list.addAll(valueList);
			}
		}
		return list.toArray();
	}
	
	/**
	 * Sets the parameters to hibernateQuery.
	 * @param hibQuery Query
	 * @param values - values
	 */
	public static void setParams(Query hibQuery, Object[] values) {
		int size = values.length;
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				hibQuery.setParameter(i, values[i]);
			}
		}
	}
	
	/**
	 * Creates a where clause using given propSpecs
	 * @param propSpecs
	 * @return where clause string
	 */
	private static String createWhereClause(List<PropertySpec> propSpecs) {
	    StringBuilder whereClause = new StringBuilder();

	    if (CollectionsUtil.isNonEmpty(propSpecs)) {
	      whereClause.append(WHERE_CLAUSE);
	      int n = propSpecs.size();
	      for (int i = 0; i < n; i++) {
	        PropertySpec propSpec = propSpecs.get(i);
	        String propertyName = propSpec.getPropertyName();
	        Object value = propSpec.getValue();
	        Operation operation = propSpec.getOperation();
	        if (Operation.IN.equals(operation)) {
	          if (! hasAtleastOneValue(value)) {
	            logger.warn("not even one value is available for IN operation: " + propertyName + ", hence ignoring this property.");
	            continue;
	          }
	        }
	        whereClause.append(propertyName).append(getOperationString(operation, value));
	        if (i < n - 1) {
	          whereClause.append(AND_CLAUSE);
	        }
	      }
	    }
	    return whereClause.toString();
	  }

	  private static boolean hasAtleastOneValue(Object value) {
	    if (value instanceof Object[]) {
	      Object[] array = (Object[]) value;
	      if (array.length == 0)
	        return false;
	    } else if (value instanceof Collection) {
	      Collection c = (Collection) value;
	      if (! CollectionsUtil.isNonEmpty(c))
	        return false;
	    }
	    return true;
	  }
	
	/**
	 * Prepares operation string out of operation and value.
	 * For eg., 
	 * 1. Operation.EQ, Long(1) -> =?
	 * 2. Operation.LIKE, "" -> LIKE: %?%
	 * 3. Operation.IN, new Object { 1, 2, 3 } -> in (?,?,?)  
	 * @param operation
	 * @param value
	 * @return
	 */
	private static String getOperationString(Operation operation, Object value) {
		String opText = EQUALS + QUESTION_MARK;
		switch (operation) {
		case EQ:
			break;

		case LIKE:
			opText = LIKE_TEXT + QUOTE + PERC + QUESTION_MARK + PERC + QUOTE ;
			break;

		case GT:
			opText = GT_TEXT + QUESTION_MARK;
			break;

		case LT:
			opText = LT_TEXT + QUESTION_MARK;
			break;

		case BETWEEN:
			opText = BETWEEN_TEXT + QUESTION_MARK + AND_CLAUSE  + QUESTION_MARK;
			break;

		case GTE:
			opText = GTE_TEXT + QUESTION_MARK;
			break;

		case LTE:
			opText = LTE_TEXT + QUESTION_MARK;
			break;

		case NOTEQUAL:
			opText = NOTEQ_TEXT + QUESTION_MARK;
			break;

		case ISNULL:
			opText = ISNULL_TEXT;
			break;
		case IS_NOT_NULL:
			opText = IS_NOT_NULL_TEXT;
			break;
		case IN:
		case NOT_IN:
			if (value == null)
				break;
			StringBuilder questionMarks = new StringBuilder();
			if (value instanceof Object[]) {
				Object[] array = (Object[]) value;
				if (array == null || array.length == 0) {
					break;
				}
				for (int i = 0, n = array.length; i < n; i++) {
					questionMarks.append(QUESTION_MARK);
					if (i < n - 1) {
						questionMarks.append(COMMA);
					}
				}
			} else if (value instanceof Collection){
				Collection list = (Collection) value;
				if (list == null || list.size() == 0) {
					break;
				}
				Collection c = (Collection) value;
				for (int i = 0, n = c.size(); i < n; i++) {
					questionMarks.append(QUESTION_MARK);
					if (i < n - 1) {
						questionMarks.append(COMMA);
					}
				}
			}
			else {
				questionMarks.append(QUESTION_MARK);
			}
			if (operation == Operation.IN) {
				opText = IN_TEXT;
			}else if (operation == Operation.NOT_IN) {
				opText = NOTIN_TEXT;
			}
			if (questionMarks.length() > 0) {
				opText += OPEN_PARANTHESIS + questionMarks.toString() + CLOSE_PARANTHESIS;
			}
			break;

		default:
			throw new IllegalArgumentException("invalid operation " + operation);
		}
		return opText;
	}
	
	/**
	 * Returns the Class<? extends Entity> of the querySpec.
	 * @param spec
	 * @return
	 * @throws DataAccessException - throws exception if querySpec is null, 
	 * if className in the QuerySpec is null or it doesn't exists in the classpath
	 */
	private static Class<? extends Entity> getClass(QuerySpec spec) throws DataAccessException {
		if (spec == null) {
			throw new DataAccessException("querySpec is null");
		}
		String className = spec.getClassName();
		if (StringUtil.nullOrEmpty(className)) {
			throw new DataAccessException("className cannot be null while creating the query");
		}
		Class<? extends Entity> clazz = null;
		try {
			// just to check whether supplied classname is there in the classpath
			 clazz = (Class<? extends Entity>) Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new DataAccessException(e);
		}
		return clazz;
	}
	
	private static List getValues0(PropertySpec propSpec) {
		List values = new ArrayList();
		Object value = propSpec.getValue();
		Operation operation = propSpec.getOperation();
		switch (operation) {
		case EQ:
		case LIKE:
		case GT:
		case LT:
		case GTE:
		case LTE:
		case NOTEQUAL:
			values.add(value);
			break;

		case ISNULL:
		case IS_NOT_NULL: break; // do nothing
		
		case BETWEEN:
			if (value instanceof Object[]) {
				Object[] array = (Object[]) value;
				for (int i = 0, n = 2; i < n; i++) { // 2 because it is Range 
					values.add(array[i]);
				}
			} else if (value instanceof List){
				List l = (List) value;
				for (int i = 0, n = 2; i < n; i++) {
					values.add(l.get(i));
				}
			}
			break;

		case IN:
		case NOT_IN:
			if (value instanceof Object[]) {
				Object[] array = (Object[]) value;
				for (int i = 0, n = array.length; i < n; i++) {  
					values.add(array[i]);
				}
			} else if (value instanceof Collection) {
				Collection c = (Collection) value;
				if (CollectionsUtil.isNonEmpty(c)) {
					Iterator itr = c.iterator();
					while (itr.hasNext()) {
						values.add(itr.next());
					}
				}
			}
			break;
		}
		return values;
	}
}
