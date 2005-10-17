package com.inetvod.common.dbdata;

/**
 * Copyright © 2004 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
public class DatabaseProcParam
{
	public int SqlType;
	public Object Value;

	public DatabaseProcParam(int sqlType, Object value)
	{
		SqlType = sqlType;
		Value = value;
	}
}
