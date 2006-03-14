/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class ProviderShowID extends StringID
{
	public static final Constructor<ProviderShowID> CtorString = CtorUtil.getCtorString(ProviderShowID.class);
	public static final int MaxLength = 128;

	public ProviderShowID(String value)
	{
		super(value);
	}
}
