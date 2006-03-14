/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.core;

//TODO: this class should be loaded from the database or from an XML file
public class SystemConfiguation
{
	/* Fields */
	private static SystemConfiguation fSystemConfiguation;

	@SuppressWarnings({"MagicNumber"})
	private long fSessionTimeoutMillis = (24 * 60 * 60 * 1000);		// 24 hours

	/* Getters and Setters */
	public long getSessionTimeoutMillis() { return fSessionTimeoutMillis; }

	/* Construction */
	public static SystemConfiguation getThe()
	{
		if(fSystemConfiguation == null)
			fSystemConfiguation = new SystemConfiguation();

		return fSystemConfiguation;
	}
}
