/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class ShowProviderID extends StringID
{
	public static final Constructor<ShowProviderID> CtorString = CtorUtil.getCtorString(ShowProviderID.class);
	public static final int MaxLength = 64;

	public ShowProviderID(String value)
	{
		super(value);
	}

	public static ShowProviderID newInstance()
	{
		return new ShowProviderID(UUID.randomUUID().toString());
	}
}
