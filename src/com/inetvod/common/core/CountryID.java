/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.lang.reflect.Constructor;

public class CountryID extends StringID
{
	public static final Constructor<CountryID> CtorString = CtorUtil.getCtorString(CountryID.class);
	public static final int MaxLength = 2;

	public static final CountryID US = new CountryID("US");

	public CountryID(String value)
	{
		super(value);
	}
}
