/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

public class MediaContainer
{
	public static final int MaxLength = 32;

	public static final MediaContainer ASF = new MediaContainer("ASF");
	public static final MediaContainer RM = new MediaContainer("RM");
	public static final MediaContainer RTSP = new MediaContainer("RTSP");
	public static final MediaContainer AVI = new MediaContainer("AVI");
	public static final MediaContainer MOV = new MediaContainer("MOV");

	private final String fValue;

	private MediaContainer(String name)
	{
		fValue = name;
	}

	public String toString()
	{
		return fValue;
	}

	public boolean equals(Object obj)
	{
		if(!(obj instanceof MediaContainer))
			return false;

		return fValue.equals(((MediaContainer)obj).fValue);
	}

	public static MediaContainer convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(ASF.fValue.equals(value))
			return ASF;
		if(RM.fValue.equals(value))
			return RM;
		if(RTSP.fValue.equals(value))
			return RTSP;
		if(AVI.fValue.equals(value))
			return AVI;
		if(MOV.fValue.matches(value))
			return MOV;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(MediaContainer value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
