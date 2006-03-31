/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.util.HashMap;

import com.inetvod.common.core.StrUtil;

public class TextType
{
	public static final int MaxLength = 32;

	public static final TextType Plain = new TextType("plain");
	public static final TextType Html = new TextType("html");
	public static final TextType Unknown = new TextType("unknown");
	private static HashMap<String, TextType> fAllValues;

	private final String fValue;

	private TextType(String name)
	{
		if(fAllValues == null)
			fAllValues = new HashMap<String, TextType>();
		fValue = name;
		fAllValues.put(name, this);
	}

	public String toString()
	{
		return fValue;
	}

	public static TextType convertFromString(String value)
	{
		if(!StrUtil.hasLen(value))
			return null;

		TextType item = fAllValues.get(value);
		if(item != null)
			return item;

		return Unknown;
	}

	public static String convertToString(TextType value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
