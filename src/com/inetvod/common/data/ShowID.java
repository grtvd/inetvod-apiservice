/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class ShowID extends StringID
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
