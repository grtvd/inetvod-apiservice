/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

public class UUStringID extends StringID
{
	public UUStringID(String value)
	{
		super((value != null) ? value.toLowerCase() : null);
	}
}
