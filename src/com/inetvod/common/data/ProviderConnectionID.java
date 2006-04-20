/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class ProviderConnectionID extends UUStringID
{
	public static final Constructor<ProviderConnectionID> CtorString = CtorUtil.getCtorString(ProviderConnectionID.class);
	public static final int MaxLength = 64;

	public ProviderConnectionID(String value)
	{
		super(value);
	}

	public static ProviderConnectionID newInstance()
	{
		return new ProviderConnectionID(UUID.randomUUID().toString());
	}
}
