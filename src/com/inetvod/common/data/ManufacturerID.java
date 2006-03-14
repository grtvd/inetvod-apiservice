/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class ManufacturerID extends StringID
{
	public static final Constructor<ManufacturerID> CtorString = CtorUtil.getCtorString(ManufacturerID.class);
	public static final int MaxLength = 32;

	public ManufacturerID(String value)
	{
		super(value);
	}
}
