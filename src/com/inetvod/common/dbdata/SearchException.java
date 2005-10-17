package com.inetvod.common.dbdata;

/**
 * Created by IntelliJ IDEA.
 * User: Bob
 * Date: Jan 16, 2004
 * Time: 11:31:16 AM
 *
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
