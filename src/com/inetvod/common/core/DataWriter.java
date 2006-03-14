/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.util.Date;
import java.util.List;

public abstract class DataWriter
{
	/**
	 * Write a Byte
	 *
	 * @param fieldName
	 * @param data
	 */
	public abstract void writeByte(String fieldName, Byte data) throws Exception;

	/**
	 * Write a byte value
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeByteValue(String fieldName, byte data) throws Exception
	{
		writeByte(fieldName, data);
	}

	/**
	 * Write a Short
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public abstract void writeShort(String fieldName, Short data) throws Exception;

	/**
	 * Write a short value
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeShortValue(String fieldName, short data) throws Exception
	{
		writeShort(fieldName, data);
	}

	/**
	 * Write an Integer
	 *
	 * @param fieldName
	 * @param data
	 */
	public abstract void writeInt(String fieldName, Integer data) throws Exception;

	/**
	 * Write an int value
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeIntValue(String fieldName, int data) throws Exception
	{
		writeInt(fieldName, data);
	}

	/**
	 * Write a Float
	 *
	 * @param fieldName
	 * @param data
	 */
	public abstract void writeFloat(String fieldName, Float data) throws Exception;

	/**
	 * Write a Double
	 *
	 * @param fieldName
	 * @param data
	 */
	public abstract void writeDouble(String fieldName, Double data) throws Exception;

	/**
	 * Write a String
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 * @throws Exception
	 */
	public abstract void writeString(String fieldName, String data, int maxLength) throws Exception;

	/**
	 * Write a date, no Time component
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public abstract void writeDate(String fieldName, Date data) throws Exception;

	/**
	 * Write a Date with a Time component
	 *
	 * @param fieldName
	 * @param data
	 * @exception Exception
	 */
	public abstract void writeDateTime(String fieldName, Date data) throws Exception;

	/**
	 * Write a Boolean
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public abstract void writeBoolean(String fieldName, Boolean data) throws Exception;

	/**
	 * Write a boolean value
	 *
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public void writeBooleanValue(String fieldName, boolean data) throws Exception
	{
		writeBoolean(fieldName, data);
	}

	/**
	 * Write a complex Object
	 * @param fieldName
	 * @param data
	 * @throws Exception
	 */
	public abstract void writeObject(String fieldName, Writeable data) throws Exception;

	/**
	 * Write a list of complex Objects
	 * @param fieldName
	 * @param data
	 */
	public abstract void writeList(String fieldName, List data) throws Exception;

	/**
	 * Write a list of Strings (or non-complex items than can be converted to a sting)
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 */
	public abstract void writeStringList(String fieldName, List data, int maxLength) throws Exception;

	/**
	 * Write a DataID Object
	 *
	 * @param fieldName
	 * @param data
	 * @param maxLength
	 * @throws Exception
	 */
	public abstract void writeDataID(String fieldName, DataID data, int maxLength) throws Exception;
}
