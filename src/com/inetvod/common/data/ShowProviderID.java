/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class ShowProviderID extends UUStringID
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
