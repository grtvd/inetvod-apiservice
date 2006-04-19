/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

public class StringList extends ArrayList<String>
{
	public static final Constructor<StringList> Ctor = CtorUtil.getCtorDefault(StringList.class);
}
