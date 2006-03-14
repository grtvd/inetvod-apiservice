/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

public class DatabaseField
{
	/* Fields */
	public String Name;
	public int Position;
	public int SqlType;
	public int SqlSize;

	/* Construction */
	public DatabaseField(String name, int position, int sqlType, int sqlSize)
	{
		Name = name;
		Position = position;
		SqlType = sqlType;
		SqlSize = sqlSize;
	}
}
