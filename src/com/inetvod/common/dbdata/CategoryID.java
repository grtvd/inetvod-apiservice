/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;

import com.inetvod.common.core.CtorUtil;
import com.inetvod.common.core.StringID;

public class CategoryID extends StringID
{
	public static final Constructor<CategoryID> CtorString = CtorUtil.getCtorString(CategoryID.class);
	public static final CategoryID Featured = new CategoryID("featured");
	public static final int MaxLength = 32;

	public CategoryID(String value)
	{
		super(value);
	}
}
