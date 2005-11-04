/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.data;

public class LicenseMethod
{
	public static final int MaxLength = 32;

	public static final LicenseMethod URLOnly = new LicenseMethod("URLOnly");
	public static final LicenseMethod LicenseServer = new LicenseMethod("LicenseServer");

	private final String fValue;

	private LicenseMethod(String name)
	{
		fValue = name;
	}

	public String toString()
	{
		return fValue;
	}

	public static LicenseMethod convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(URLOnly.fValue.equals(value))
			return URLOnly;
		if(LicenseServer.fValue.equals(value))
			return LicenseServer;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(LicenseMethod value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
