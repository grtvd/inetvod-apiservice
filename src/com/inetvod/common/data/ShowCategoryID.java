/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class ShowCategoryID extends UUStringID
{
	public static final Constructor<ShowCategoryID> CtorString = CtorUtil.getCtorString(ShowCategoryID.class);
	public static final int MaxLength = 64;

	public ShowCategoryID(String value)
	{
		super(value);
	}

	public static ShowCategoryID newInstance()
	{
		return new ShowCategoryID(UUID.randomUUID().toString());
	}
}
