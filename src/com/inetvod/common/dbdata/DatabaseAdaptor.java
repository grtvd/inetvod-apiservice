/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;

import com.inetvod.common.core.DataExists;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Logger;

public class DatabaseAdaptor<T extends DatabaseObject, L extends List<T>>
{
	private static final String DatabaseName = "iNetVOD";	//TODO: move to configuration
	private static final String SchemaName = "dbo";	//TODO: move to configuration
	//private static final int MetaDataTableColumnName = 4;
	//private static final int MetaDataTableColumnSqlType = 5;
	//private static final int MetaDataTableColumnSqlSize = 7;
	//private static final int MetaDataTableColumnPos = 17;
	private static final int MetaDataProcedureName = 3;
	private static final int MetaDataProcedureColumnName = 4;
	private static final int MetaDataProcedureColumnSqlType = 6;
	private static final int MetaDataProcedureColumnSqlSize = 8;

	private static final String GetProcedureSuffix = "_Get";
	private static final String InsertProcedureSuffix = "_Insert";
	private static final String UpdateProcedureSuffix = "_Update";
	private static final String DeleteProcedureSuffix = "_Delete";

	@SuppressWarnings({"FieldCanBeLocal"})
	private Class<T> fObjectType;
	private String fObjectName;
	private Constructor<T> fObjectCtor;
	@SuppressWarnings({"FieldCanBeLocal"})
	private Class<L> fListType;
	private Constructor<L> fListCtor;

	private HashMap<String, DatabaseField> fFields;

	private String fGetStoredProcedure;
	private String fInsertStoredProcedure;
	private String fUpdateStoredProcedure;
	private String fDeleteStoredProcedure;

	protected DatabaseAdaptor(Class<T> objectType, Class<L> listType)
	{
		fObjectType = objectType;
		fListType = listType;

		try
		{
			fObjectCtor = fObjectType.getConstructor(DataReader.class);
			fListCtor = fListType.getConstructor();
		}
		catch(NoSuchMethodException e)
		{
			Logger.logErr(this, "ctor", e);
			return;
		}

		String name = objectType.getName();
		String[] nameParts = name.split("\\.");
		fObjectName = nameParts[nameParts.length - 1];

		initialize();
		int numFields = fFields.size();

		fGetStoredProcedure = buildProcName(fObjectName + GetProcedureSuffix, 1);
		fInsertStoredProcedure = buildProcName(fObjectName + InsertProcedureSuffix, numFields);
		fUpdateStoredProcedure = buildProcName(fObjectName + UpdateProcedureSuffix, numFields);
		fDeleteStoredProcedure = buildProcName(fObjectName + DeleteProcedureSuffix, 1);
	}

	private void initialize()
	{
		try
		{
			Connection connection = getConnection();

			initFields(connection);
			confirmProcedures(connection);
		}
		catch(Exception e)
		{
			Logger.logErr(this, "ctor", e);
		}
	}

	private void initFields(Connection connection)
	{
		fFields = new HashMap<String, DatabaseField>();

		getFieldsFromUpdateProcedures(connection, true);
		if(fFields.size() == 0)
			getFieldsFromUpdateProcedures(connection, false);

		if(fFields.size() == 0)
			Logger.logWarn(this, "initFields", String.format("No write procedures found for table(%s)", fObjectName));	//TODO: convert to logDebug
	}

	private void getFieldsFromUpdateProcedures(Connection connection, boolean useInsert)
	{
		String procedureName = fObjectName + (useInsert ? InsertProcedureSuffix : UpdateProcedureSuffix);
		ResultSet resultSet;
		String fieldName;
		int fieldPos;
		int fieldSqlType;
		int fieldSqlSize;

		try
		{
			if(fFields.size() > 0)
				throw new Exception(String.format("Table (%s) already initialized", fObjectName));

			//Logger.logInfo(this, "getFieldsFromUpdateProcedures", String.format("Confirming table (%s) via (%s)", fObjectName, procedureName));	//TODO: convert to logDebug

			fieldPos = 0;
			resultSet = connection.getMetaData().getProcedureColumns(DatabaseName, SchemaName, procedureName, null);
			while(resultSet.next())
			{
				fieldName = resultSet.getString(MetaDataProcedureColumnName).replaceAll("@", "");

				if("RETURN_VALUE".equals(fieldName))
					continue;

				fieldSqlType = resultSet.getInt(MetaDataProcedureColumnSqlType);
				fieldSqlSize = resultSet.getInt(MetaDataProcedureColumnSqlSize);
				fieldPos++;
				//Logger.logInfo(this, "getFieldsFromUpdateProcedures", String.format("Field: %s/%d/%d/%d", fieldName, fieldPos, fieldSqlType, fieldSqlSize));

				fFields.put(fieldName, new DatabaseField(fieldName, fieldPos, fieldSqlType, fieldSqlSize));
			}
		}
		catch(Exception e)
		{
			Logger.logErr(this, "getFieldsFromUpdateProcedures", e);
		}
	}

	private void confirmProcedures(Connection connection)
	{
		try
		{
			ResultSet resultSet;
			String procedureName;
			String procedureNameMatch = fObjectName + "%";
			boolean allFields;

			if(fFields.size() == 0)
				return;

			//Logger.logInfo(this, "confirmProcedures", String.format("Confirming table(%s)", fObjectName));	//TODO: convert to logDebug

			resultSet = connection.getMetaData().getProcedureColumns(DatabaseName, SchemaName, procedureNameMatch,
				"@RETURN_VALUE");
			procedureNameMatch = fObjectName + "_";		// for search '_' is wildcard, so must manually match against '_'
			while(resultSet.next())
			{
				procedureName = resultSet.getString(MetaDataProcedureName);
				if(procedureName.startsWith(procedureNameMatch))
				{
					//Logger.logInfo(this, "confirmProcedures", String.format("Confirming procedure(%s)", procedureName));	//TODO: convert to logDebug

					allFields = (procedureName.endsWith(InsertProcedureSuffix)
						|| procedureName.endsWith(UpdateProcedureSuffix));
					confirmProcedureParms(connection, procedureName, allFields);
				}
			}
		}
		catch(Exception e)
		{
			Logger.logErr(this, "confirmProcedureParms", e);
		}
	}

	private void confirmProcedureParms(Connection connection, String procedureName, boolean allFields)
	{
		try
		{
			ResultSet resultSet;
			DatabaseField databaseField;
			String fieldName;
			int fieldPos = 0;
			int fieldSqlType;
			int fieldSqlSize;

			resultSet = connection.getMetaData().getProcedureColumns(DatabaseName, SchemaName, procedureName, null);
			while(resultSet.next())
			{
				fieldName = resultSet.getString(MetaDataProcedureColumnName).replaceAll("@", "");
				fieldSqlType = resultSet.getInt(MetaDataProcedureColumnSqlType);
				fieldSqlSize = resultSet.getInt(MetaDataProcedureColumnSqlSize);
				//Logger.logInfo(this, "confirmProcedureParms", String.format("Field: %s/%d/%d", fieldName, fieldSqlType, fieldSqlSize));

				if("RETURN_VALUE".equals(fieldName))
					continue;

				databaseField = fFields.get(fieldName);

				if(allFields)
				{
					// if this case, databaseField should not be null

					fieldPos++;
					if(fieldPos != databaseField.Position)
						Logger.logWarn(this, "confirmProcedureParms", String.format("Database field(%s) position mismatch(%d/%d) for procedure(%s)",
							fieldName, fieldPos, databaseField.Position, procedureName));
				}
				else if(databaseField == null)
					continue;

				if(fieldSqlType != databaseField.SqlType)
					Logger.logWarn(this, "confirmProcedureParms", String.format("Database field(%s) sqlType mismatch(%d/%d) for procedure(%s)",
						fieldName, fieldSqlType, databaseField.SqlType, procedureName));
				if(fieldSqlSize != databaseField.SqlSize)
					Logger.logWarn(this, "confirmProcedureParms", String.format("Database field(%s) sqlSize mismatch(%d/%d) for procedure(%s)",
						fieldName, fieldSqlSize, databaseField.SqlSize, procedureName));
			}
		}
		catch(Exception e)
		{
			Logger.logErr(this, "confirmProcedureParms", e);
		}
	}

	private Connection getConnection() throws Exception
	{
		Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver");
		Connection conn = DriverManager.getConnection("jdbc:microsoft:sqlserver://localhost","inetvod","1v0d");
		conn.setCatalog(DatabaseName);
		return conn;
	}

	public T selectByKey(Object key, DataExists exists) throws Exception
	{
		Connection connection = null;
		CallableStatement statement = null;
		ResultSet resultSet;

		try
		{
			connection = getConnection();

			statement = connection.prepareCall(fGetStoredProcedure);
			statement.setString(1, key.toString());
			resultSet = statement.executeQuery();

			return readOne(resultSet, exists, true);
		}
		finally
		{
			if(statement != null)
				statement.close();
			if(connection != null)
				connection.close();
		}
	}

	public T select(String command, DataExists exists) throws Exception
	{
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet;

		try
		{
			connection = getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(command);

			return readOne(resultSet, exists, true);
		}
		finally
		{
			if(statement != null)
				statement.close();
			if(connection != null)
				connection.close();
		}
	}

	public T selectByProc(String prodecure, DatabaseProcParam[] params, DataExists exists) throws Exception
	{
		Connection connection = null;
		CallableStatement statement = null;
		ResultSet resultSet;

		try
		{
			connection = getConnection();

			statement = connection.prepareCall(buildProcName(prodecure, (params == null) ? 0 : params.length));
			setProcParams(statement, params);
			resultSet = statement.executeQuery();

			return readOne(resultSet, exists, true);
		}
		finally
		{
			if(statement != null)
				statement.close();
			if(connection != null)
				connection.close();
		}
	}

	public L selectManyByProc(String prodecure, DatabaseProcParam[] params) throws Exception
	{
		L newList = fListCtor.newInstance();
		Connection connection = null;
		CallableStatement statement = null;
		ResultSet resultSet;
		T newObject;

		try
		{
			connection = getConnection();

			statement = connection.prepareCall(buildProcName(prodecure, (params == null) ? 0 : params.length));
			setProcParams(statement, params);
			resultSet = statement.executeQuery();

			while((newObject = readOne(resultSet, DataExists.MayNotExist, false)) != null)
				newList.add(newObject);
		}
		finally
		{
			if(statement != null)
				statement.close();
			if(connection != null)
				connection.close();
		}

		return newList;
	}

	protected String buildProcName(String procedure, int numParams)
	{
		String fields = "";

		if(numParams > 0)
		{
			StringBuilder sb = new StringBuilder("?");
			for(int i = 1; i < numParams; i++)
				sb.append(", ?");
			fields = sb.toString();
		}

		return "{call " + procedure + "(" + fields +")}";
	}

	protected void setProcParams(PreparedStatement statement, DatabaseProcParam[] params) throws SQLException
	{
		DatabaseProcParam param;

		if(params != null)
		{
			for(int i = 0; i < params.length; i++)
			{
				param = params[i];

				if(param.Value == null)
					statement.setNull(i + 1, param.SqlType);
				else if(param.SqlType == Types.VARCHAR)
					statement.setString(i + 1, (String)param.Value);
				else if(param.SqlType == Types.SMALLINT)
					statement.setShort(i + 1, (Short)param.Value);
				else if(param.SqlType == Types.INTEGER)
					statement.setInt(i + 1, (Integer)param.Value);
				else
					throw new IllegalArgumentException();
			}
		}
	}

	protected T readOne(ResultSet resultSet, DataExists exists, boolean maxOne) throws SQLException, SearchException
	{
		if(!resultSet.next())
		{
			if(exists.equals(DataExists.MayNotExist))
				return null;
			throw new SearchException("No Record Found");
		}

		try
		{
			DatabaseFieldReader reader = new DatabaseFieldReader(resultSet, fFields);
			T newObject = fObjectCtor.newInstance(reader);
			newObject.setNewRecord(false);

			if(!maxOne || !resultSet.next())
				return newObject;
			throw new SearchException("Too Many Records Found");
		}
		catch(Exception e)
		{
			throw new SearchException("Read Record Failed", e);
		}
	}

	public void update(T databaseObject) throws Exception
	{
		Connection connection = null;
		PreparedStatement statement = null;

		try
		{
			DatabaseFieldWriter writer;

			connection = getConnection();

			if(databaseObject.isNewRecord())
				statement = connection.prepareStatement(fInsertStoredProcedure);
			else
				statement = connection.prepareStatement(fUpdateStoredProcedure);

			writer = new DatabaseFieldWriter(statement, fFields);
			databaseObject.writeTo(writer);
			writer.close();

			int result = statement.executeUpdate();
			if(result != 1)
				throw new UpdateException("update failed, result(" + result + ")");
			databaseObject.setNewRecord(false);
		}
		finally
		{
			if(statement != null)
				statement.close();
			if(connection != null)
				connection.close();
		}
	}

	public void delete(Object key) throws Exception
	{
		Connection connection = null;
		CallableStatement statement = null;

		try
		{
			connection = getConnection();

			statement = connection.prepareCall(fDeleteStoredProcedure);
			statement.setString(1, key.toString());

			int result = statement.executeUpdate();
			if(result != 1)
				throw new UpdateException("delete failed, result(" + result + ")");
		}
		finally
		{
			if(statement != null)
				statement.close();
			if(connection != null)
				connection.close();
		}
	}

/*
	private void metaDataCheck() throws Exception
	{
		Connection connection = null;
		//PreparedStatement statement = null;
		CallableStatement statement = null;
		ResultSet resultSet;

		connection = getConnection();
		//resultSet = connection.getMetaData().getSchemas();
		resultSet = connection.getMetaData().getProcedureColumns("iNetVOD", "dbo", fObjectName + "_Insert", null);
		//resultSet = connection.getMetaData().getColumns(DatabaseName, SchemaName, fObjectName, null);
		while(resultSet.next())
		{
			for(int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++)
				System.out.print(resultSet.getString(i) + "|");
			System.out.println();
		}
	}
*/
}
