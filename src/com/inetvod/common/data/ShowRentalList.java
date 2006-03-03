/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ShowRentalList extends ArrayList<ShowRental>
{
	public static final Constructor<ShowRentalList> Ctor = CtorUtil.getCtorDefault(ShowRentalList.class);

	public void copy(ShowRentalList showRentalList)
	{
		clear();
		addAll(showRentalList);
	}
}
