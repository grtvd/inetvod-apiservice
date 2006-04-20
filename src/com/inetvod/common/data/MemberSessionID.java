/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class MemberSessionID extends UUStringID
{
	public static final Constructor<MemberSessionID> CtorString = CtorUtil.getCtorString(MemberSessionID.class);
	public static final int MaxLength = 64;

	public MemberSessionID(String value)
	{
		super(value);
	}

	public static MemberSessionID newInstance()
	{
		return new MemberSessionID(UUID.randomUUID().toString());
	}
}
