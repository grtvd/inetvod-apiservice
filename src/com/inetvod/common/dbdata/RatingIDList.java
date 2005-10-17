/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class RatingIDList extends ArrayList<RatingID>
{
	public static final Constructor<RatingIDList> Ctor = CtorUtil.getCtorDefault(RatingIDList.class);
}