/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ShowIDList extends ArrayList<ShowID>
{
	public static final Constructor<ShowIDList> Ctor = CtorUtil.getCtorDefault(ShowIDList.class);
}
