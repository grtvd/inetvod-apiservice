/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class MemberSessionID extends StringID
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
