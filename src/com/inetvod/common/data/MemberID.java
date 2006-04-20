/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class MemberID extends UUStringID
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
