/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class CategoryMapList extends ArrayList<CategoryMap>
{
	public static final Constructor<CategoryMapList> Ctor = CtorUtil.getCtorDefault(CategoryMapList.class);
}
