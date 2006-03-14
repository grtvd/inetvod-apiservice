/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

public class DataFormat
{
	public static final int MaxLength = 32;

	public static final DataFormat XML = new DataFormat("XML");
	public static final DataFormat Binary = new DataFormat("Binary");

	private final String fValue;

	private DataFormat(String name)
	{
		fValue = name;
	}

	public String toString()
	{
		return fValue;
	}

	public static DataFormat convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(XML.fValue.equals(value))
			return XML;
		if(Binary.fValue.equals(value))
			return Binary;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(DataFormat value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
