/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.lang.reflect.Constructor;

public class LanguageID extends StringID
{
	public static final Constructor<LanguageID> CtorString = CtorUtil.getCtorString(LanguageID.class);
	public static final int MaxLength = 3;

	public static final LanguageID English = new LanguageID("eng");

	public LanguageID(String value)
	{
		super(value);
	}
}
