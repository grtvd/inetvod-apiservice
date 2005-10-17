/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
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
