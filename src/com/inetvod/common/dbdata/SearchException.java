/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

/**
 * This exception should be used when searching for data is not expected to fail.
 * As the condition when searching via a primary key.
 */
public class SearchException extends Exception
{
	public SearchException(String message)
	{
		super(message);
	}

	public SearchException(String message, Throwable cause)
	{
		super(message, cause);
	}
}
