/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

public class DataExists
{
	public static final DataExists MustExist = new DataExists();
	public static final DataExists MayNotExist = new DataExists();

	private DataExists()
	{
	}
}
