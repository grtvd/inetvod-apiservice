/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

public class MediaEncoding
{
	public static final int MaxLength = 32;

	public static final MediaEncoding WMV9 = new MediaEncoding("WMV9");
	public static final MediaEncoding RV9 = new MediaEncoding("RV9");
	public static final MediaEncoding SVQ3 = new MediaEncoding("SVQ3");
	public static final MediaEncoding DivX5 = new MediaEncoding("DivX5");
	public static final MediaEncoding Xvid = new MediaEncoding("Xvid");

	private final String fValue;

	private MediaEncoding(String name)
	{
		fValue = name;
	}

	public String toString()
	{
		return fValue;
	}

	public static MediaEncoding convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(WMV9.fValue.equals(value))
			return WMV9;
		if(RV9.fValue.equals(value))
			return RV9;
		if(SVQ3.fValue.equals(value))
			return SVQ3;
		if(DivX5.fValue.equals(value))
			return DivX5;
		if(Xvid.fValue.equals(value))
			return Xvid;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(MediaEncoding value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
