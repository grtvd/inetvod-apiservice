/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.data.ProviderShowID;

public class ReleaseShowRqst implements Writeable
{
	/* Fields */
	protected ProviderShowID fShowID;

	/* Construction */
	private ReleaseShowRqst(ProviderShowID showID)
	{
		fShowID = showID;
	}

	public static ReleaseShowRqst newInstance(ProviderShowID showID)
	{
		return new ReleaseShowRqst(showID);
	}

	/* Implementation */
	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ShowID", fShowID, ProviderShowID.MaxLength);
	}
}
