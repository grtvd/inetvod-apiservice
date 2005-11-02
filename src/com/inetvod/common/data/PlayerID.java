/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class PlayerID extends StringID
{
	public static final Constructor<PlayerID> CtorString = CtorUtil.getCtorString(PlayerID.class);
	public static final int MaxLength = 64;

	public PlayerID(String value)
	{
		super(value);
	}
}
