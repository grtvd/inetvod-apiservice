/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class ProviderID extends StringID
{
	public static final Constructor<ProviderID> CtorString = CtorUtil.getCtorString(ProviderID.class);
	public static final int MaxLength = 32;

	public ProviderID(String value)
	{
		super(value);
	}
}
