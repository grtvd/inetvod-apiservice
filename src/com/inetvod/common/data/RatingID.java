/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class RatingID extends StringID
{
	public static final Constructor<RatingID> CtorString = CtorUtil.getCtorString(RatingID.class);
	public static final RatingID NotRated = new RatingID("notrated");
	public static final int MaxLength = 32;

	public RatingID(String value)
	{
		super(value);
	}
}
