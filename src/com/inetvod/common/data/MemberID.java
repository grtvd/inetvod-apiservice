/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class MemberID extends StringID
{
	public static final Constructor<MemberID> CtorString = CtorUtil.getCtorString(MemberID.class);
	public static final int MaxLength = 64;

	public MemberID(String value)
	{
		super(value);
	}

	public static MemberID newInstance()
	{
		return new MemberID(UUID.randomUUID().toString());
	}
}
