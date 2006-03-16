/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.inetvod.common.core.DataID;
import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.DateUtil;
import com.inetvod.common.core.Readable;

public class DatabaseFieldReader extends DataReader
{
	private ResultSet fResultSet;
	String[] fFieldNameList;
	private List<String> fFieldNamePrefixList = new ArrayList<String>();

	public DatabaseFieldReader(ResultSet resultSet, HashMap<String, DatabaseField> fields)
	{
		fResultSet = resultSet;
		fFieldNameList = new String[fields.size()];
		fields.keySet().toArray(fFieldNameList);
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

	private boolean areAllChildrenNull(String parentName) throws SQLException
	{
		String childPrefix = buildFullFieldName(parentName + "_");

		for(String fieldName : fFieldNameList)
		{
			if(fieldName.startsWith(childPrefix))
			{
				fResultSet.getObject(fieldName);
				if(!fResultSet.wasNull())
					return false;
			}
		}

		return true;
	}

	/**
	 * Read a Byte.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Byte readByte(String fieldName) throws Exception
	{
		return null;  //To change body of implemented methods use File | Settings | File Templates.
	}

	/**
	 * Read a Short.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Short readShort(String fieldName) throws Exception
	{
		short value = fResultSet.getShort(buildFullFieldName(fieldName));

		if(fResultSet.wasNull())
			return null;

		return value;
	}

	/**
	 * Read a Integer.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Integer readInt(String fieldName) throws Exception
	{
		int value = fResultSet.getInt(buildFullFieldName(fieldName));

		if(fResultSet.wasNull())
			return null;

		return value;
	}

	/**
	 * Read a Float.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Float readFloat(String fieldName) throws Exception
	{
		float value = fResultSet.getFloat(buildFullFieldName(fieldName));

		if(fResultSet.wasNull())
			return null;

		return value;
	}

	/**
	 * Read a Double.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Double readDouble(String fieldName) throws Exception
	{
		double value = fResultSet.getDouble(buildFullFieldName(fieldName));

		if(fResultSet.wasNull())
			return null;

		return value;
	}

	/**
	 * Read a String.
	 *
	 * @param fieldName
	 * @param maxLength
	 * @return may return null
	 */
	public String readString(String fieldName, int maxLength) throws Exception
	{
		String data = fResultSet.getString(buildFullFieldName(fieldName));

		if ((data == null) || (data.length() == 0))
			return null;

		if(data.length() > maxLength)
			throw new Exception("invalid len(" + data.length() + "), maxLength(" + maxLength +" )");

		return data;
	}

	/**
	 * Read a Date, no Time component.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Date readDate(String fieldName) throws Exception
	{
		Date value = fResultSet.getDate(buildFullFieldName(fieldName));

		if(fResultSet.wasNull())
			return null;

		return value;
	}

	/**
	 * Read a Date with a Time compnent.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Date readDateTime(String fieldName) throws Exception
	{
		Date value = DateUtil.convertFromDBTimestamp(fResultSet.getTimestamp(buildFullFieldName(fieldName)));

		if(fResultSet.wasNull())
			return null;

		return value;
	}

	/**
	 * Read a Boolean.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Boolean readBoolean(String fieldName) throws Exception
	{
		boolean value = fResultSet.getBoolean(buildFullFieldName(fieldName));

		if(fResultSet.wasNull())
			return null;

		return value;
	}

	/**
	 * Read an Object.
	 *
	 * @param fieldName
	 * @param ctorDataReader
	 * @return may return null
	 */
	public Readable readObject(String fieldName, Constructor ctorDataReader) throws Exception
	{
		// If all child fields of object are null, consider object to be null
		if(areAllChildrenNull(fieldName))
			return null;

		fFieldNamePrefixList.add(fieldName + "_");
		Readable readable = (Readable)ctorDataReader.newInstance(this);
		fFieldNamePrefixList.remove(fFieldNamePrefixList.size() - 1);

		return readable;
	}

	/**
	 * Read a list of complex Objects.
	 *
	 * @param fieldName
	 * @param listCtor
	 * @param itemCtorDataReader
	 * @return will never return null, may return an empty list
	 */
	public List readList(String fieldName, Constructor listCtor, Constructor itemCtorDataReader) throws Exception
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
	}

	/**
	 * Read a list of Strings (or non-complex items than can be constructed from a sting).
	 *
	 * @param fieldName
	 * @param maxLength
	 * @param listCtor
	 * @param itemCtorString
	 * @return will never return null, may return an empty list
	 */
	public <T, L extends List<T>> L readStringList(String fieldName, int maxLength, Constructor<L> listCtor,
		Constructor<T> itemCtorString) throws Exception
	{
		L list = listCtor.newInstance();

		String data = fResultSet.getString(buildFullFieldName(fieldName));
		if ((data == null) || (data.length() == 0))
			return list;

		String[] items = data.split(",");

		for(String itemStr : items)
		{
			T item = itemCtorString.newInstance(itemStr);
			list.add(item);
		}

		return list;
	}

	/**
	 * Read a DataID object.
	 *
	 * @param fieldName
	 * @param maxLength
	 * @param ctorString
	 * @return may return null
	 */
	public <T extends DataID> T readDataID(String fieldName, int maxLength, Constructor<T> ctorString) throws Exception
	{
		String data = readString(fieldName, maxLength);

		if (data == null)
			return null;

		return ctorString.newInstance(data);
	}
}
