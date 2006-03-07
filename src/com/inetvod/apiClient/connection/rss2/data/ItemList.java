/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class ItemList extends ArrayList<Item>
{
	public static final Constructor<ItemList> Ctor = CtorUtil.getCtorDefault(ItemList.class);
}
