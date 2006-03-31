/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.util.HashMap;

public class MediaContentMedium
{
	public static final int MaxLength = 32;

	public static final MediaContentMedium Image = new MediaContentMedium("image ");
	public static final MediaContentMedium Audio = new MediaContentMedium("audio ");
	public static final MediaContentMedium Video = new MediaContentMedium("video ");
	public static final MediaContentMedium Document = new MediaContentMedium("document ");
	public static final MediaContentMedium Executable = new MediaContentMedium("executable");
	public static final MediaContentMedium Unknown = new MediaContentMedium("unknown");
	private static HashMap<String, MediaContentMedium> fAllValues;

	private final String fValue;

	private MediaContentMedium(String name)
	{
		if(fAllValues == null)
			fAllValues = new HashMap<String, MediaContentMedium>();
		fValue = name;
		fAllValues.put(name, this);
	}

	public String toString()
	{
		return fValue;
	}

	public static MediaContentMedium convertFromString(String value)
	{
		if((value == null) || (value.length() == 0))
			return null;

		MediaContentMedium item = fAllValues.get(value);
		if(item != null)
			return item;

		return Unknown;
	}

	public static String convertToString(MediaContentMedium value)
	{
		if(value == null)
			return null;
		return value.toString();
	}
}
