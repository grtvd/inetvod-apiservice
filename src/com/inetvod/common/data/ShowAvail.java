/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.util.HashMap;

public class ShowAvail
{
	public static final int MaxLength = 32;

	public static final ShowAvail Available = new ShowAvail("Available");
	public static final ShowAvail Confirming = new ShowAvail("Confirming");
	public static final ShowAvail Unavailable = new ShowAvail("Unavailable");
	private static HashMap<String, ShowAvail> fAllValues;

	private final String fValue;

	private ShowAvail(String name)
	{
		if(fAllValues == null)
			fAllValues = new HashMap<String, ShowAvail>();
		fValue = name;
		fAllValues.put(name, this);
	}

	public String toString()
	{
		return fValue;
	}

	public static ShowAvail convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		ShowAvail showAvail = fAllValues.get(value);
		if(showAvail != null)
			return showAvail;

		throw new IllegalArgumentException("bad value(" + value + ")");
	}

	public static String convertToString(ShowAvail value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
