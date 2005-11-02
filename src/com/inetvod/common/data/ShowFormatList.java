/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ShowFormatList extends ArrayList<ShowFormat>
{
	public static final Constructor<ShowFormatList> Ctor = CtorUtil.getCtorDefault(ShowFormatList.class);
}
