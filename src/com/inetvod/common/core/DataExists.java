/**
 * Copyright © 2004 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
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
