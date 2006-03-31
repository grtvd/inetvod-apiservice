/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.inetvod.common.core.DataID;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.DateUtil;
import com.inetvod.common.core.Writeable;

public class DatabaseFieldWriter extends DataWriter
{
	private PreparedStatement fStatement;		// for writing
	private HashMap<String, DatabaseField> fFields;
	private HashSet<String> fFieldsWritten;
	private List<String> fFieldNamePrefixList = new ArrayList<String>();

	public DatabaseFieldWriter(PreparedStatement statement, HashMap<String, DatabaseField> fields)
	{
		fStatement = statement;
		fFields = fields;
		fFieldsWritten = new HashSet<String>();
	}

	public void close() throws SQLException
	{
		for(DatabaseField databaseField : fFields.values())
		{
			if(!fFieldsWritten.contains(databaseField.Name))
				fStatement.setNull(databaseField.Position, databaseField.SqlType);
		}
	}

	private String buildFullFieldName(String fieldName)
	{
		if(fFieldNamePrefixList.size() == 0)
			return fieldName;

		StringBuffer sb = new StringBuffer();
		for(String namePrefix : fFieldNamePrefixList)
			sb.append(namePrefix);
		sb.append(fieldName);

		return sb.toString();
	}

	private DatabaseField getField(String fieldName)
	{
		fFieldsWritten.add(fieldName);
		return fFields.get(fieldName);
	}

	/**
	 * Write a Byte
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeByte(String fieldName, Byte data) throws Exception
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
	}

	/**
	 * Write a Short
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeShort(String fieldName, Short data) throws Exception
	{
		int fieldPosition = getField(buildFullFieldName(fieldName)).Position;
		if (data != null)
			fStatement.setShort(fieldPosition, data);
		else
			fStatement.setNull(fieldPosition, Types.SMALLINT);
	}

	/**
	 * Write an Integer
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeInt(String fieldName, Integer data) throws Exception
	{
		int fieldPosition = getField(buildFullFieldName(fieldName)).Position;
		if (data != null)
			fStatement.setInt(fieldPosition, data);
		else
			fStatement.setNull(fieldPosition, Types.INTEGER);
	}

	/**
	 * Write a Float
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeFloat(String fieldName, Float data) throws Exception
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
	}

	/**
	 * Write a Double
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeDouble(String fieldName, Double data) throws Exception
	{
		int fieldPosition = getField(buildFullFieldName(fieldName)).Position;
		if (data != null)
			fStatement.setDouble(fieldPosition, data);
		else
			fStatement.setNull(fieldPosition, Types.DOUBLE);
	}

	/**
	 * Write a String
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 * @throws Exception
	 */
	public void writeString(String fieldName, String data, int maxLength) throws Exception
	{
		if((data != null) && (data.length() > maxLength))
			throw new Exception(String.format("invalid len(%d), maxLength(%d), for fieldName(%s)", data.length(), maxLength, fieldName));

		//TODO: might need to update this to handle 'text' (blob) fields larger than 4000 chars
		int fieldPosition = getField(buildFullFieldName(fieldName)).Position;
		fStatement.setString(fieldPosition, ((data == null) || (data.length() == 0)) ? null : data);
	}

	/**
	 * Write a date, no Time component
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeDate(String fieldName, Date data) throws Exception
	{
		int fieldPosition = getField(buildFullFieldName(fieldName)).Position;
		fStatement.setDate(fieldPosition, (data != null) ? DateUtil.convertToDBDate(data) : null);
	}

	/**
	 * Write a Date with a Time component
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeDateTime(String fieldName, Date data) throws Exception
	{
		int fieldPosition = getField(buildFullFieldName(fieldName)).Position;
		fStatement.setTimestamp(fieldPosition, (data != null) ? DateUtil.convertToDBTimestamp(data) : null);
	}

	/**
	 * Write a Boolean
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeBoolean(String fieldName, Boolean data) throws Exception
	{
		int fieldPosition = getField(buildFullFieldName(fieldName)).Position;
		if(data != null)
			fStatement.setBoolean(fieldPosition, data);
		else
			fStatement.setNull(fieldPosition, Types.BOOLEAN);
	}

	/**
	 * Write a complex Object
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeObject(String fieldName, Writeable data) throws Exception
	{
		if(data == null)
			return;

		fFieldNamePrefixList.add(fieldName + "_");
		data.writeTo(this);
		fFieldNamePrefixList.remove(fFieldNamePrefixList.size() - 1);
	}

	/**
	 * Write a list of complex Objects
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeList(String fieldName, List data)
	{
		throw new UnsupportedOperationException("writeList not supported for databases");
	}

	/**
	 * Write a list of Strings (or non-complex items than can be converted to a sting)
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 */
	public void writeStringList(String fieldName, List data, int maxLength) throws Exception
	{
		StringBuilder sb = new StringBuilder();

		for(Object item : data)
		{
			if(sb.length() > 0)
				sb.append(",");
			sb.append(item.toString());
		}

		writeString(fieldName, sb.toString(), maxLength);
	}

	/**
	 * Write a DataID Object
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 * @throws Exception
	 */
	public void writeDataID(String fieldName, DataID data, int maxLength) throws Exception
	{
		writeString(fieldName, (data != null) ? data.toString() : null, maxLength);
	}
}
