/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.rqdata;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ShowDetailList extends ArrayList<ShowDetail>
{
	public static final Constructor<ShowDetailList> Ctor = CtorUtil.getCtorDefault(ShowDetailList.class);
}
