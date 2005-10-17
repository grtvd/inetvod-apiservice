/**
 * Copyright © 2004 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.core;

public class StringID extends DataID
{
	protected final String fValue;

	public StringID(String value)
	{
		if ((value == null) || (value.length() == 0))
			throw new IllegalArgumentException("value is undefined");

		fValue = value;
	}

	public String toString()
	{
		return fValue;
	}

	public boolean equals(Object o)
	{
		if ((o == null) || !(o instanceof StringID))
			return false;

		return fValue.equals(((StringID)o).fValue);
	}
}
