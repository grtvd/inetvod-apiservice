/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class MemberProviderID extends StringID
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
