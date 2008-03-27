/**
 * Copyright © 2006-2008 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.util.HashMap;

public class ITunesExplicit
{
	public static final int MaxLength = 32;

	public static final ITunesExplicit Yes = new ITunesExplicit("yes");
	public static final ITunesExplicit No = new ITunesExplicit("no");
	public static final ITunesExplicit Clean = new ITunesExplicit("clean");
	private static HashMap<String, ITunesExplicit> fAllValues;

	private final String fValue;

	private ITunesExplicit(String name)
	{
		if(fAllValues == null)
			fAllValues = new HashMap<String, ITunesExplicit>();
		fValue = name;
		fAllValues.put(name, this);
	}

	public String toString()
	{
		return fValue;
	}

	public static ITunesExplicit convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		ITunesExplicit item = fAllValues.get(value.toLowerCase());
		if(item != null)
			return item;

		return null;
	}

	public static String convertToString(ITunesExplicit value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
