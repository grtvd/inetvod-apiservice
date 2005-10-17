/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

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
