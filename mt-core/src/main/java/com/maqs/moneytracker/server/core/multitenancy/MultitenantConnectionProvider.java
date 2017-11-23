package com.maqs.moneytracker.server.core.multitenancy;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.service.jdbc.connections.spi.MultiTenantConnectionProvider;

public class MultitenantConnectionProvider implements
		MultiTenantConnectionProvider {

	private static final long serialVersionUID = 4368575201221677384L;

	private static final String SPACE = " ";

	private DataSource dataSource = null;

	private Logger logger = Logger.getLogger(getClass());
	
	private String schemaChangeCommand;
	
	private String defaultSchema;
	
	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public boolean isUnwrappableAs(Class clazz) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> clazz) {
		return null;
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		final Connection connection = dataSource.getConnection();
		return connection;
	}

	@Override
	public Connection getConnection(String tenantIdentifier)
			throws SQLException {
		final Connection connection = getAnyConnection();
		try {
			logger.debug("setting schema for connection [" + tenantIdentifier
					+ "]");
			connection.createStatement().execute(
					schemaChangeCommand + SPACE + tenantIdentifier);
		} catch (SQLException e) {
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema: "
							+ tenantIdentifier, e);
		}
		return connection;
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		try {
			logger.debug("releaseAnyConnection() setting defaultSchema: " + defaultSchema);
			connection.createStatement().execute(schemaChangeCommand + SPACE + defaultSchema);
		} catch (SQLException e) {
			throw new HibernateException(
					"Could not alter JDBC connection to specified schema: " + defaultSchema,
					e);
		} finally {
			if (connection != null) 
				connection.close();
		}
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection)
			throws SQLException {
		releaseAnyConnection(connection);
	}
	
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}
	
	public void setSchemaChangeCommand(String schemaChangeCommand) {
		this.schemaChangeCommand = schemaChangeCommand;
	}
	
	public String getSchemaChangeCommand() {
		return schemaChangeCommand;
	}
	
	public void setDefaultSchema(String defaultSchema) {
		this.defaultSchema = defaultSchema;
	}
	
	public String getDefaultSchema() {
		return defaultSchema;
	}
}
