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
