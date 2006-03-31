/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection.rss2.data;

import java.lang.reflect.Constructor;
import java.util.ArrayList;

import com.inetvod.common.core.CtorUtil;

public class MediaContentList extends ArrayList<MediaContent>
{
	public static final Constructor<MediaContentList> Ctor = CtorUtil.getCtorDefault(MediaContentList.class);
}
