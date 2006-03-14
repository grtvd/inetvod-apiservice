/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class RatingIDList extends ArrayList<RatingID>
{
	public static final Constructor<RatingIDList> Ctor = CtorUtil.getCtorDefault(RatingIDList.class);
}