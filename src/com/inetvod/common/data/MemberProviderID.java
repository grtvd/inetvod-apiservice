/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class MemberProviderID extends UUStringID
{
	public static final Constructor<MemberProviderID> CtorString = CtorUtil.getCtorString(MemberProviderID.class);
	public static final int MaxLength = 64;

	public MemberProviderID(String value)
	{
		super(value);
	}

	public static MemberProviderID newInstance()
	{
		return new MemberProviderID(UUID.randomUUID().toString());
	}
}
