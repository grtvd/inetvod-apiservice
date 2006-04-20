/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class ShowID extends UUStringID
{
	public static final Constructor<ShowID> CtorString = CtorUtil.getCtorString(ShowID.class);
	public static final int MaxLength = 64;

	public ShowID(String value)
	{
		super(value);
	}

	public static ShowID newInstance()
	{
		return new ShowID(UUID.randomUUID().toString());
	}
}
