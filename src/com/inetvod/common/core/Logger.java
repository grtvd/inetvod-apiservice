/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

public class Logger
{
	private static org.apache.log4j.Logger getLogger(Class logClass)
	{
		return org.apache.log4j.Logger.getLogger(logClass);
	}

	public static void logInfo(Class objClass, String method, String message)
	{
		getLogger(objClass).info(String.format("%s|%s", method, ((message != null) ? message : "")));
	}

	public static void logInfo(Class objClass, String method, Exception e)
	{
		getLogger(objClass).info(String.format("%s|", method), e);
	}

	public static void logInfo(Object objClass, String method, String message)
	{
		logInfo(objClass.getClass(), method, message);
	}

	public static void logInfo(Object objClass, String method, Exception e)
	{
		logInfo(objClass.getClass(), method, e);
	}

	public static void logWarn(Class objClass, String method, String message)
	{
		getLogger(objClass).warn(String.format("%s|%s", method, ((message != null) ? message : "")));
	}

	public static void logWarn(Class objClass, String method, Exception e)
	{
		getLogger(objClass).warn(String.format("%s|", method), e);
	}

	public static void logWarn(Object objClass, String method, String message)
	{
		logWarn(objClass.getClass(), method, message);
	}

	public static void logWarn(Object objClass, String method, Exception e)
	{
		logWarn(objClass.getClass(), method, e);
	}

	public static void logErr(Class objClass, String method, String message, Exception e)
	{
		getLogger(objClass).error(String.format("%s|%s", method, ((message != null) ? message : "")), e);
	}

	public static void logErr(Class objClass, String method, Exception e)
	{
		getLogger(objClass).error(String.format("%s|", method), e);
	}

	public static void logErr(Object objClass, String method, String message, Exception e)
	{
		logErr(objClass.getClass(), method, message, e);
	}

	public static void logErr(Object objClass, String method, Exception e)
	{
		logErr(objClass.getClass(), method, e);
	}
}
