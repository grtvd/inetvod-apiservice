/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;

public class ShowListRqst implements Writeable
{
	/* Construction */
	private ShowListRqst()
	{
	}

	public static ShowListRqst newInstance()
	{
		return new ShowListRqst();
	}

	/* Implementation */
	public void writeTo(DataWriter writer) throws Exception
	{
		// No Fields
	}
}
