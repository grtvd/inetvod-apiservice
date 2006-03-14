/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.rqdata;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ShowDetailList extends ArrayList<ShowDetail>
{
	public static final Constructor<ShowDetailList> Ctor = CtorUtil.getCtorDefault(ShowDetailList.class);
}
