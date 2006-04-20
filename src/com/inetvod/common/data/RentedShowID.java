/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.UUID;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class RentedShowID extends UUStringID
{
	public static final Constructor<RentedShowID> CtorString = CtorUtil.getCtorString(RentedShowID.class);
	public static final int MaxLength = 64;

	public RentedShowID(String value)
	{
		super(value);
	}

	public static RentedShowID newInstance()
	{
		return new RentedShowID(UUID.randomUUID().toString());
	}
}
