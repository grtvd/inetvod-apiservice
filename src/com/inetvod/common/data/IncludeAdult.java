/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

public class IncludeAdult
{
	public static final int MaxLength = 32;

	public static final IncludeAdult Never = new IncludeAdult("Never");
	public static final IncludeAdult PromptPassword = new IncludeAdult("PromptPassword");
	public static final IncludeAdult Always = new IncludeAdult("Always");

	private final String fValue;

	private IncludeAdult(String value)
	{
		fValue = value;
	}

	public String toString()
	{
		return fValue;
	}

	public static IncludeAdult convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		if(Never.fValue.equals(value))
			return Never;
		if(PromptPassword.fValue.equals(value))
			return PromptPassword;
		if(Always.fValue.equals(value))
			return Always;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(IncludeAdult includeAdult)
	{
		if(includeAdult == null)
			return null;
		return includeAdult.toString();
	}
}
