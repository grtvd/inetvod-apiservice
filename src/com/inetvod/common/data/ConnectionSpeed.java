/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

public class ConnectionSpeed
{
	public static final int MaxLength = 32;

	public static final ConnectionSpeed S300K = new ConnectionSpeed("300K");
	public static final ConnectionSpeed S700K = new ConnectionSpeed("700K");
	public static final ConnectionSpeed S1500K = new ConnectionSpeed("1500K");
	public static final ConnectionSpeed LAN = new ConnectionSpeed("LAN");

	private final String fValue;

	private ConnectionSpeed(String name)
	{
		fValue = name;
	}

	public String toString()
	{
		return fValue;
	}

	public static ConnectionSpeed convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(S300K.fValue.equals(value))
			return S300K;
		if(S700K.fValue.equals(value))
			return S700K;
		if(S1500K.fValue.equals(value))
			return S1500K;
		if(LAN.fValue.equals(value))
			return LAN;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(ConnectionSpeed value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
