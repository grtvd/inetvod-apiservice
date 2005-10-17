/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.core;

import java.lang.reflect.Constructor;

public class CurrencyID extends StringID
{
	public static final Constructor<CurrencyID> CtorString = CtorUtil.getCtorString(CurrencyID.class);
	public static final int MaxLength = 3;

	public static final CurrencyID USD = new CurrencyID("USD");

	public CurrencyID(String value)
	{
		super(value);
	}
}
