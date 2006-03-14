/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class CategoryIDList extends ArrayList<CategoryID>
{
	public static final Constructor<CategoryIDList> Ctor = CtorUtil.getCtorDefault(CategoryIDList.class);

	public void copy(CategoryIDList categoryIDList)
	{
		clear();
		addAll(categoryIDList);
	}
}