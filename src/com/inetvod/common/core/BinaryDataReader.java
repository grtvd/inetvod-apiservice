/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.io.DataInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.List;

public class BinaryDataReader extends DataReader
{
	protected static final short UndefinedShortValue = Short.MIN_VALUE;
	protected static final double UndefinedDoubleValue = Float.MIN_VALUE;
	protected static final byte UndefinedBooleanValue = (byte)255;

	protected InputStream fInputStream;
	protected ByteOrder fByteOrder;
	protected DataInputStream fDataInputStream;

	public BinaryDataReader(InputStream inputStream, ByteOrder byteOrder)
	{
		fInputStream = inputStream;
		fByteOrder = byteOrder;
		fDataInputStream = new DataInputStream(fInputStream);
	}

	/**
	 * Read a Byte.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Byte readByte(String fieldName) throws Exception
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
	}

	/**
	 * Read a Short.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Short readShort(String fieldName) throws Exception
	{
		short data = fDataInputStream.readShort();

		if(data == UndefinedShortValue)
			return null;

		return data;
	}

	/**
	 * Read a Integer.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Integer readInt(String fieldName) throws Exception
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
	}

	/**
	 * Read a short.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Float readFloat(String fieldName) throws Exception
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
	}

	/**
	 * Read a Double.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Double readDouble(String fieldName) throws Exception
	{
		double data = fDataInputStream.readDouble();

		if(data == UndefinedDoubleValue)
			return null;

		return data;
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
		String data = fDataInputStream.readUTF();
		if((data == null) || (data.length() == 0))
			return null;
		int len = data.length();

		if(len > maxLength)
			throw new Exception("invalid len(" + len + "), maxLength(" + maxLength + ")");

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
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
	}

	/**
	 * Read a Date with a Time compnent.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Date readDateTime(String fieldName) throws Exception
	{
		throw new UnsupportedOperationException("need to implement");	//TODO: need to implement
	}

	/**
	 * Read a Boolean.
	 *
	 * @param fieldName
	 * @return may return null
	 */
	public Boolean readBoolean(String fieldName) throws Exception
	{
		byte data = fDataInputStream.readByte();

		if(data == UndefinedBooleanValue)
			return null;

		return (data != 0);
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
		boolean isNotNull = (fDataInputStream.readByte() != 0);
		if(!isNotNull)
			return null;

		return (Readable)ctorDataReader.newInstance(new Object[] { this });
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
	public List readStringList(String fieldName, int maxLength, Constructor listCtor, Constructor itemCtorString) throws Exception
	{
		List list = (List)listCtor.newInstance(new Object[] {});

		int count = fDataInputStream.readInt();
		if(count == 0)
			return list;

		String item;
		for(int i = 0; i < count; i++)
		{
			item = readString(fieldName, maxLength);
			list.add(itemCtorString.newInstance(new Object[] { item }));
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
		String data = fDataInputStream.readUTF();
		if((data == null) || (data.length() == 0))
			return null;

		return ctorString.newInstance(new Object[] { data });
	}
}
