/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.ByteOrder;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

public class BinaryDataWriter extends DataWriter
{
	protected static final byte UndefinedByteValue = (byte)255;
	protected static final short UndefinedShortValue = Short.MIN_VALUE;
	protected static final int UndefinedIntValue = Integer.MIN_VALUE;
	protected static final float UndefinedFloatValue = Float.MIN_VALUE;	//TODO: this is not correct
	protected static final double UndefinedDoubleValue = Float.MIN_VALUE;
	protected static final byte UndefinedBooleanValue = (byte)255;

	protected OutputStream fOutputStream;
	protected ByteOrder fByteOrder;
	protected DataOutputStream fDataOutputStream;

	public BinaryDataWriter(OutputStream outputStream, ByteOrder byteOrder)
	{
		fOutputStream = outputStream;
		fByteOrder = byteOrder;
		fDataOutputStream = new DataOutputStream(fOutputStream);
	}

	/**
	 * Write a Byte
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeByte(String fieldName, Byte data) throws Exception
	{
		fDataOutputStream.writeByte(data != null ? (data) : UndefinedByteValue);
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
		fDataOutputStream.writeShort(data != null ? (data) : UndefinedShortValue);
	}

	/**
	 * Write an Integer
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeInt(String fieldName, Integer data) throws Exception
	{
		fDataOutputStream.writeInt(data != null ? (data) : UndefinedIntValue);
	}

	/**
	 * Write a Float
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeFloat(String fieldName, Float data) throws Exception
	{
		fDataOutputStream.writeFloat(data != null ? (data) : UndefinedFloatValue);
	}

	/**
	 * Write a Double
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeDouble(String fieldName, Double data) throws Exception
	{
		fDataOutputStream.writeDouble(data != null ? (data) : UndefinedDoubleValue);
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
		int len = (data != null) ? data.length() : 0;
		if(len > maxLength)
			throw new Exception("invalid len(" + len + "), maxLength(" + maxLength + ")");

		if (len == 0)
			fDataOutputStream.writeShort(0);
		else
			fDataOutputStream.writeUTF(data);	// can't pass null
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
		if(data == null)
		{
			writeShortValue(fieldName, (short)0);
			writeByteValue(fieldName, (byte)0);
			writeByteValue(fieldName, (byte)0);
		}
		else
		{
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(data);

			writeShortValue(fieldName, (short)(cal.get(Calendar.YEAR)));
			writeByteValue(fieldName, (byte)(cal.get(Calendar.MONTH) + 1));
			writeByteValue(fieldName, (byte)(cal.get(Calendar.DAY_OF_MONTH)));
		}
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
		if(data == null)
		{
			writeShortValue(fieldName, (short)0);
			writeByteValue(fieldName, (byte)0);
			writeByteValue(fieldName, (byte)0);
			writeByteValue(fieldName, (byte)0);
			writeByteValue(fieldName, (byte)0);
			writeByteValue(fieldName, (byte)0);
		}
		else
		{
			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
			cal.setTime(data);

			writeShortValue(fieldName, (short)(cal.get(Calendar.YEAR)));
			writeByteValue(fieldName, (byte)(cal.get(Calendar.MONTH) + 1));
			writeByteValue(fieldName, (byte)(cal.get(Calendar.DAY_OF_MONTH)));
			writeByteValue(fieldName, (byte)(cal.get(Calendar.HOUR_OF_DAY)));
			writeByteValue(fieldName, (byte)(cal.get(Calendar.MINUTE)));
			writeByteValue(fieldName, (byte)(cal.get(Calendar.SECOND)));
		}
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
		fDataOutputStream.writeByte(data != null ? ((data) ? 1 : 0) : UndefinedByteValue);
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
		fDataOutputStream.writeByte((data != null) ? 1 : 0);
		if(data == null)
			return;

		data.writeTo(this);
	}

	/**
	 * Write a list of complex Objects
	 *
	 * @param fieldName
	 * @param data
	 */
	public void writeList(String fieldName, List data) throws Exception
	{
		writeIntValue(fieldName, data.size());

		for(Object item : data)
			((Writeable)item).writeTo(this);
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
		Iterator iter = data.iterator();

		writeIntValue(fieldName, data.size());

		while(iter.hasNext())
			writeString(fieldName, iter.next().toString(), maxLength);
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
