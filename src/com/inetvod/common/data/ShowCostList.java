/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ShowCostList extends ArrayList<ShowCost>
{
	/* Constants */
	public static final Constructor<ShowCostList> Ctor = CtorUtil.getCtorDefault(ShowCostList.class);
}
