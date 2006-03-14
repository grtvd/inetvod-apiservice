/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;

public class PingRqst implements Writeable
{
	/* Construction */
	public static PingRqst newInstance()
	{
		return new PingRqst();
	}

	/* Implementation */
	public void writeTo(DataWriter writer)
	{
		// No Fields
	}
}
