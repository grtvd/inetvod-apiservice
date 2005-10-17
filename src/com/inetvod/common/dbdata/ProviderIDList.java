/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ProviderIDList extends ArrayList<ProviderID>
{
	public static final Constructor<ProviderIDList> Ctor = CtorUtil.getCtorDefault(ProviderIDList.class);
}