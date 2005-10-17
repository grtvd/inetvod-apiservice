/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ShowCostList extends ArrayList<ShowCost>
{
	/* Constants */
	public static final Constructor<ShowCostList> Ctor = CtorUtil.getCtorDefault(ShowCostList.class);
}
