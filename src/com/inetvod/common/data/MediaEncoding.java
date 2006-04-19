/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.util.HashMap;

public class MediaEncoding
{
	public static final int MaxLength = 32;

	public static final MediaEncoding WMV9 = new MediaEncoding("WMV9");
	public static final MediaEncoding RV9 = new MediaEncoding("RV9");
	public static final MediaEncoding SVQ3 = new MediaEncoding("SVQ3");
	public static final MediaEncoding DivX5 = new MediaEncoding("DivX5");
	public static final MediaEncoding Xvid = new MediaEncoding("Xvid");
	public static final MediaEncoding MP3 = new MediaEncoding("MP3");
	private static HashMap<String, MediaEncoding> fAllValues;

	private final String fValue;

	private MediaEncoding(String name)
	{
		if(fAllValues == null)
			fAllValues = new HashMap<String, MediaEncoding>();
		fValue = name;
		fAllValues.put(name, this);
	}

	public String toString()
	{
		return fValue;
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof MediaEncoding))
			return false;

		return fValue.equals(((MediaEncoding)obj).fValue);
	}

	public static MediaEncoding convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		MediaEncoding mediaEncoding = fAllValues.get(value);
		if(mediaEncoding != null)
			return mediaEncoding;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(MediaEncoding value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
