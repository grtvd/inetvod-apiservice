/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.inetvod.common.core.DataID;
import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;

public class DatabaseFieldWriter extends DataWriter
{
	protected PreparedStatement fStatement;		// for writing
	protected int fCurParam;

	public DatabaseFieldWriter(PreparedStatement statement)
	{
		fStatement = statement;
		fCurParam = 1;
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
		if (data != null)
			fStatement.setShort(fCurParam++, data);
		else
			fStatement.setNull(fCurParam++, Types.SMALLINT);
	}

	/**
	 * Write an Integer
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeInt(String fieldName, Integer data) throws Exception
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
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
		if (data != null)
			fStatement.setDouble(fCurParam++, data);
		else
			fStatement.setNull(fCurParam++, Types.DOUBLE);
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
			throw new Exception("invalid len(" + data.length() + "), maxLength(" + maxLength +" )");

		fStatement.setString(fCurParam++, ((data == null) || (data.length() == 0)) ? null : data);
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
		fStatement.setDate(fCurParam++, (data != null) ? new java.sql.Date (data.getTime()) : null);
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
		fStatement.setTimestamp(fCurParam++, (data != null) ? new Timestamp (data.getTime()) : null);
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
		if(data != null)
			fStatement.setBoolean(fCurParam++, data);
		else
			fStatement.setNull(fCurParam, Types.BOOLEAN);
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
			throw new UnsupportedOperationException("null objects currently not supported");	//TODO: need to implement

		data.writeTo(this);
	}

	/**
	 * Write a list of complex Objects
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeList(String fieldName, List data)
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
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
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
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
