package com.inetvod.common.dbdata;

/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
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
