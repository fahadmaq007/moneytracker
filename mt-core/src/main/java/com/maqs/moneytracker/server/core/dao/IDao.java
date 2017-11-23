package com.maqs.moneytracker.server.core.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;

import com.maqs.moneytracker.common.paging.Page;
import com.maqs.moneytracker.common.paging.spec.Operation;
import com.maqs.moneytracker.common.paging.spec.QuerySpec;
import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.server.core.exception.DataAccessException;

public interface IDao {

	/**
	 * Performs the save operation.
	 * @param e Entity to be stored
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public Entity save(Entity e) throws DataAccessException;
	
	/**
	 * Performs the update operation.
	 * @param e Entity to be stored
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public void update(Entity e) throws DataAccessException;
	
	/**
	 * Performs the saveOrUpdate operation.
	 * @param e Entity to be stored
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public Entity saveOrUpdate(Entity e) throws DataAccessException;
	
	/**
	 * Performs the saveAll operation.
	 * @param list list of entities to be saved.
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public List<? extends Entity> saveAll(List<? extends Entity> list) throws DataAccessException;
	
	
	/**
	 * Retrieves the entity of a given class with the corresponding id.
	 * @param clazz Class
	 * @param id Identifier
	 * @return Entity
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public Entity getEntity(Class<? extends Entity> clazz, Long id) throws DataAccessException;
	
	/**
	 * Retrieves the entity of a given class where propertyName=value
	 * @param clazz Class
	 * @param propertyName Property Name
	 * @param value value
	 * @return Entity
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public Entity getEntity(Class<? extends Entity> clazz, String propertyName, Object value) throws DataAccessException;
	
	/**
	 * Removes the given entity.
	 * @param clazz Entity of Class
	 * @param id Identifier
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public void removeEntity(Class<? extends Entity> clazz, Long id) throws DataAccessException;
	
	/**
	 * Removes all the entities
	 * @param entities
	 * @throws DataAccessException
	 */
	public void removeAll(List<? extends Entity> entities) throws DataAccessException;
	/**
	 * Gets the total number of records available
	 * @param querySpec query spec
	 * @return count(*)
	 * @throws DataAccessException
	 */
	public long count(QuerySpec querySpec) throws DataAccessException;
	
	/**
	 * Retrieves the list of entities filtering throw query spec.
	 * @param spec query spec
	 * @param page page
	 * @return list of entities
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public List<? extends Entity> listAll(QuerySpec spec, Page page) throws DataAccessException;
	
	/**
	 * Retrieves the list of entities filtering throw query spec.
	 * @param spec query spec
	 * @return list of entities
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public List<? extends Entity> listAll(QuerySpec spec) throws DataAccessException;
	
	/**
	 * Retrieves the list of child entities querying the parentId.
	 * @param clazz list of entities to be picked
	 * @param propertyName Property name of the parentProperty
	 * @param parentId parentId to look for
	 * @return list of child entities
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public List<? extends Entity> listAll(Class<? extends Entity> clazz, String propertyName, Long parentId) throws DataAccessException;
	
	/**
	 * Fetches the entities by IN operation.
	 * @param clazz Class entities to be picked
	 * @param propertyName onProperty
	 * @param idList values
	 * @return Map<id, entity>
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public Map<Long, ? extends Entity> listByIds(Class<? extends Entity> clazz, String propertyName, Collection idList) throws DataAccessException;
	/**
	 * Fetches the entities by IN operation.
	 * @param childClazz Class entities to be picked
	 * @param propertyName propertyName of the parent
	 * @param idList ids
	 * @return Map<parentId, List<child>>
	 * @throws DataAccessException throws {@link DataAccessException} in case of any sql exceptions
	 */
	public Map<Long, List<? extends Entity>> listChildrenByParentIds(Class<? extends Entity> childClazz, String propertyName, Collection idList) throws DataAccessException;
	/**
	 * Checks whether the record exists with the supplied id.
	 * @param clazz entity class to look for
	 * @param id identifier
	 * @return true if exists, false otherwise.
	 * @throws DataAccessException
	 */
	public boolean containsById(Class<? extends Entity> clazz, Long id) throws DataAccessException;
	
	/**
	 * Checks whether the record exists where propertyName = value.
	 * @param clazz entity class to look for
	 * @param propertyName Property Name of the class
	 * @param value Value of the field 
	 * @return true if exists, false otherwise.
	 * @throws DataAccessException
	 */
	public boolean containsByProperty(Class<? extends Entity> clazz, String propertyName, Object value) throws DataAccessException;
	
	/**
	 * Checks whether the record exists as per the querySpec
	 * @param querySpec querySpec for the translation
	 * @return true if exists, false otherwise.
	 * @throws DataAccessException
	 */
	public boolean contains(QuerySpec querySpec) throws DataAccessException;
	
	/**
	 * Remove all records from the Class matching the criteria.
	 * @param clazz entity class name
	 * @param propertyName property name to query for
	 * @param value value of the propertyName
	 * @return no. of records deleted
	 * @throws DataAccessException
	 */
	public int removeAll(Class<? extends Entity> clazz, String propertyName, Object value) throws DataAccessException;
	
	/**
	 * Remove all records from the Class matching the criteria.
	 * @param clazz entity class name
	 * @param propertyName property name to query for
	 * @param operation what operation
	 * @param value value of the propertyName
	 * @return no. of records deleted
	 */
	public int removeAll(Class<? extends Entity> clazz,
			String propertyName, Operation operation, Object value) throws DataAccessException;
	
	/**
	 * Removes as per the querySpec.
	 * 
	 * @param spec QuerySpec carries the className - entity, List<PropertySpec> - criteria.
	 * @return no. of records deleted
	 * @throws DataAccessException
	 */
	public int removeAll(QuerySpec spec) throws DataAccessException;
	
	/**
	 * This method loads the selective properties of the Entity.
	 * In the querySpec the following can be sent which will be translated to the respective query.
	 * <ul>
	 * <li>SelectSpec</li> - defines the select statement, 
	 * both 'selective properties of a class'. For Eg. select new Project(reference) from Project
	 * <li>ProjectSpec</li> - defines the where clause.	
	 * </ul>
	 * @param querySpec
	 * @param page
	 * @return
	 * @throws DataAccessException
	 */
	public List<? extends Entity> listSelectiveProperties(QuerySpec querySpec, Page page) throws DataAccessException;
	
	/**
	 * List the entities from a named HQL. For Eg.
	 * <pre>
	 * 	{@code 
	 * <query name="SysOptionDetail.getSysOptionDtlBySysOptionName">	
			<![CDATA[select sod from SystemOptionDetail sod, SystemOption so where 
			so.name = ? and sod.systemOptionId = so.id 
			and (sod.code = ? or sod.description = ?)]]></query>
		}
	 * </pre>
	 * The query should be an HQL, simple or complex, with a mandate of returning only one type.
	 * For instance, the above query returns the SystemOptionDetail entities.
	 * 
	 * It can also retrieve List<? extends Entity>, the below query shows the same:
	 * <pre>
	 * {@code 
	 * 	<query name="SysOptionDetail.getSysOptionDetailDto">	
			<![CDATA[select new SystemOptionDetail(sod.code, sod.description) from 
			SystemOptionDetail sod, SystemOption so where so.name = ? and sod.systemOptionId = so.id 
			and (sod.code = ? or sod.description = ?)]]></query>
		}
	 * </pre>
	 * 
	 * @param namedQueryId name of the query
	 * @param paramValues parameter values 
	 * @return List of entities
	 * @throws DataAccessException
	 */
	public List<? extends Entity> listEntitiesByNamedQuery(String namedQueryId, Object[] paramValues) throws DataAccessException;
	
	/**
	 * Gets the unique result from a named HQL.
	 * For Eg.
	 * <pre>
	 * 	{@code 
	 * 	<query name="SysOptionDetail.countByCodeNDesc">	
			<![CDATA[select count(s.id) from SystemOptionDetail s where s.systemOptionId = ? 
			and (s.code = ? or s.description = ?)]]>
		</query>
		}
	 * </pre>
	 * The above query returns the Long object.
	 * 
	 * It can also return multiple columns, for instance, the below query returns Object[].
	 * <pre>
	 * 	{@code 
	 * 	<query name="SysOptionDetail.countByCodeNDesc">	
			<![CDATA[select count(s.id), max(s.id) from SystemOptionDetail s where s.systemOptionId = ? 
			and (s.code = ? or s.description = ?)]]>
		</query>
		}
		</pre>
	 * @param namedQueryId name of the query
	 * @param paramValues parameter values 
	 * @return
	 * @throws DataAccessException
	 */
	public Object uniqueResultByNamedQuery(String namedQueryId, Object[] paramValues) throws DataAccessException;
	
	/**
	 * Executes any HQL & returns the List<Object[]>
	 * For Eg.
	 * <pre>
	 * 	{@code 
	 * 	<query name="SysOptionDetail.getCodeNDesc">	
			<![CDATA[select s.code, s.description from SystemOptionDetail s where s.systemOptionId = ? 
			and (s.code = ? or s.description = ?)]]>
		</query>
		}
	 * </pre>
	 * 
	 * It can also retrieve List<DTO>, the below query shows the same:
	 * <pre>
	 * 	{@code 
	 * 	<query name="SysOptionDetail.getSysOptionDetailDto">	
			<![CDATA[select new SystemOptionDetail(sod.code, sod.description) from 
			SystemOptionDetail sod, SystemOption so where so.name = ? and sod.systemOptionId = so.id 
			and (sod.code = ? or sod.description = ?)]]>
		</query>
		}
	 * </pre>
	 * Unlike listEntitiesByNamedQuery(), it can list both 
	 * managed List<? extends Entity> as well as non-managed List<AnyDTO> entities.
	 * 
	 * @param namedQueryId name of the query
	 * @param paramValues parameter values
	 * @return List<Object[]>
	 * @throws DataAccessException
	 */
	public List executeNamedQuery(final String namedQueryId, 
			final Object[] paramValues) throws DataAccessException;
	
	/**
	 * Executes any SQL query & returns the List<Object[]>.
	 * For Eg.
	 * <pre>
	 * 	{@code 
	 * 	<sql-query name="roles">
			<return-scalar column="ITEMKEY" type="string"/>
			<return-scalar column="ITEMVALUE" type="string"/>	
		select name as ITEMKEY, description as ITEMVALUE from role where id = ?
		</sql-query>
		}
	 * </pre>
	 * It's a mandate to have return-scalar values.
	 * 
	 * @param namedQueryId name of the query
	 * @param paramValues parameter values
	 * @return List<Object[]>
	 * @throws DataAccessException
	 */
	public List executeNamedSQLQuery(final String namedQueryId, 
			final Object[] paramValues) throws DataAccessException;
	
	/**
	 * Executes any SQL query & returns the List<Object[]>.
	 * For Eg.
	 * <pre>
	 * 	{@code 
	 * 	<sql-query name="roles">
			<return-scalar column="ITEMKEY" type="string"/>
			<return-scalar column="ITEMVALUE" type="string"/>	
		select name as ITEMKEY, description as ITEMVALUE from role where id = ?
		</sql-query>
		}
	 * </pre>
	 * It's a mandate to have return-scalar values.
	 * 
	 * @param namedQueryId name of the query
	 * @param paramValues parameter values
	 * @param transformClass The objects to be transformed with this class 
	 * @return List<Object[]> or List<TransformedClassObject>
	 * @throws DataAccessException
	 */
	public List executeNamedSQLQuery(final String namedQueryId, 
			final Object[] paramValues, 
			final Class transformClass) throws DataAccessException;
	
	/**
	 * Runs the update query.
	 * @param namedQueryId
	 * @param paramValues
	 * @throws DataAccessException
	 */
	public void updateNamedSQLQuery(final String namedQueryId, 
			final Object[] paramValues) throws DataAccessException;

	/**Gets an entity based on the Query Specification
	 * @param spec
	 * @return
	 * @throws DataAccessException
	 */
	public Entity getEntity(QuerySpec spec) throws DataAccessException;
	
	/**Gets an entity based on the Query Specification
	 * @param spec
	 * @return
	 * @throws DataAccessException
	 */
	public List executeNamedQuery(final String namedQueryId, 
			QuerySpec spec) throws DataAccessException;
}
