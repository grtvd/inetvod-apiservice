/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

public class StrUtil
{
	public static boolean hasLen(String str)
	{
		return ((str != null) && (str.length() > 0));
	}

	public static String removeHtml(String html)
	{
		StringBuilder sb = new StringBuilder();

		boolean inTag = false;

		for(char ch : html.toCharArray())
		{
			if(ch == '<')
				inTag = true;
			else if(ch == '>')
				inTag = false;
			else if((ch != '\n') && (ch != '\r'))
			{
				if(!inTag)
					sb.append(ch);
			}
		}

		return sb.toString().trim();
	}
}
