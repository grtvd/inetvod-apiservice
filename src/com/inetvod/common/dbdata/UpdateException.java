/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

/**
 * This exception should be used when searching for data is not expected to fail.
 * As the condition when searching via a primary key.
 */
public class UpdateException extends Exception
{
	public UpdateException(String message)
	{
		super(message);
	}

	public UpdateException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
