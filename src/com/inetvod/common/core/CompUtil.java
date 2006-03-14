/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

public class CompUtil
{
	//public static int compare(String lhs, String rhs)

	public static <T> boolean areEqual(T lhs, T rhs)
	{
		if((lhs == null) && (rhs == null))
			return true;
		if((lhs == null) || (rhs == null))
			return false;
		return lhs.equals(rhs);
	}
}
