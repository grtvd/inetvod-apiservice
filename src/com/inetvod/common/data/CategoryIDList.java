/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class CategoryIDList extends ArrayList<CategoryID>
{
	public static final Constructor<CategoryIDList> Ctor = CtorUtil.getCtorDefault(CategoryIDList.class);
}