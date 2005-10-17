/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.rqdata;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ShowRentalList extends ArrayList<ShowRental>
{
	public static final Constructor<ShowRentalList> Ctor = CtorUtil.getCtorDefault(ShowRentalList.class);
}
