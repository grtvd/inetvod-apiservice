/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.UUStringID;

public class PlayerID extends UUStringID
{
	public static final Constructor<PlayerID> CtorString = CtorUtil.getCtorString(PlayerID.class);
	public static final int MaxLength = 64;

	public PlayerID(String value)
	{
		super(value);
	}
}
