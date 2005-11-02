/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class RatingID extends StringID
{
	public static final Constructor<RatingID> CtorString = CtorUtil.getCtorString(RatingID.class);
	public static final int MaxLength = 32;

	public RatingID(String value)
	{
		super(value);
	}
}
