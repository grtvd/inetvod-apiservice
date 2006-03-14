/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

public abstract class DataReader
{
	public static <T> Constructor<T> getCtor(Class<T> cl)
	{
		try
		{
			return cl.getConstructor(DataReader.class);
		}
		catch(Exception e)
		{
		}

		return null;
	}

	/**
	 * Read a Byte.
	 * @param fieldName
	 * @return may return null
	 */
	public abstract Byte readByte(String fieldName) throws Exception;

	/**
	 * Read a Short.
	 * @param fieldName
	 * @return may return null
	 */
	public abstract Short readShort(String fieldName) throws Exception;

	/**
	 * Read a Integer.
	 * @param fieldName
	 * @return may return null
	 */
	public abstract Integer readInt(String fieldName) throws Exception;

	/**
	 * Read a Integer.
	 * @param fieldName
	 * @return will throw exception on null value
	 * @exception Exception
	 */
	public int readIntValue(String fieldName) throws Exception
	{
		Integer value = readInt(fieldName);

		if(value == null)
			throw new Exception("value is null");

		return value;
	}

	/**
	 * Read a Float.
	 * @param fieldName
	 * @return may return null
	 */
	public abstract Float readFloat(String fieldName) throws Exception;

	/**
	 * Read a Double.
	 * @param fieldName
	 * @return may return null
	 */
	public abstract Double readDouble(String fieldName) throws Exception;

	/**
	 * Read a String.
	 * @param fieldName
	 * @param maxLength
	 * @return may return null
	 */
	public abstract String readString(String fieldName, int maxLength) throws Exception;

	/**
	 * Read a Date, no Time component.
	 * @param fieldName
	 * @return may return null
	 */
	public abstract Date readDate(String fieldName) throws Exception;

	/**
	 * Read a Date with a Time compnent.
	 * @param fieldName
	 * @return may return null
	 */
	public abstract Date readDateTime(String fieldName) throws Exception;

	/**
	 * Read a Boolean.
	 * @param fieldName
	 * @return may return null
	 */
	public abstract Boolean readBoolean(String fieldName) throws Exception;

	/**
	 * Read a boolean.
	 * @param fieldName
	 * @return will throw exception on null value
	 * @exception Exception
	 */
	public boolean readBooleanValue(String fieldName) throws Exception
	{
		Boolean value = readBoolean(fieldName);

		if(value == null)
			throw new Exception("value is null");

		return value;
	}

	/**
	 * Read an Object.
	 * @param fieldName
	 * @param ctorDataReader
	 * @return may return null
	 */
	//public abstract Readable readObject(String fieldName, Constructor ctorDataReader) throws Exception;
	public abstract <T extends Readable> T readObject(String fieldName, Constructor<T> ctorDataReader) throws Exception;

	/**
	 * Read a list of complex Objects.
	 * @param fieldName
	 * @param listCtor
	 * @param itemCtorDataReader
	 * @return will never return null, may return an empty list
	 */
	//public abstract List readList(String fieldName, Constructor listCtor, Constructor itemCtorDataReader) throws Exception;
	public abstract <T, L extends List<T>> L readList(String fieldName, Constructor<L> listCtor, Constructor<T> itemCtorDataReader) throws Exception;

	/**
	 * Read a list of Strings (or non-complex items than can be constructed from a sting).
	 * @param fieldName
	 * @param maxLength
	 * @param listCtor
	 * @param itemCtorString
	 * @return will never return null, may return an empty list
	 */
	//public abstract List readStringList(String fieldName, int maxLength, Constructor listCtor, Constructor itemCtorString) throws Exception;
	public abstract <T, L extends List<T>> L readStringList(String fieldName, int maxLength, Constructor<L> listCtor,
		Constructor<T> itemCtorString) throws Exception;

	/**
	 * Read a DataID object.
	 * @param fieldName
	 * @param maxLength
	 * @param ctorString
	 * @return may return null
	 */
	public abstract <T extends DataID> T readDataID(String fieldName, int maxLength, Constructor<T> ctorString) throws Exception;
}
