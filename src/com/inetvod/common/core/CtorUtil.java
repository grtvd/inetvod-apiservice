/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.core;

import java.lang.reflect.Constructor;

public class CtorUtil
{
	public static <T> Constructor<T> getCtorDefault(Class<T> cl)
	{
		try
		{
			return cl.getConstructor(new Class[] {} );
		}
		catch(Exception e)
		{
		}

		return null;
	}
	public static <T> Constructor<T> getCtorString(Class<T> cl)
	{
		try
		{
			return cl.getConstructor(new Class[] { String.class } );
		}
		catch(Exception e)
		{
		}

		return null;
	}
}
