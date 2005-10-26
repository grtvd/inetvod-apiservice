/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.dbdata.ShowID;
import com.inetvod.common.dbdata.ProviderShowID;
import com.inetvod.common.dbdata.ShowFormat;

public class CheckShowAvailRqst implements Writeable
{
	/* Fields */
	private ProviderShowID fShowID;
	private ShowFormat fShowFormat;

	/* Construction */
	private CheckShowAvailRqst(ProviderShowID showID, ShowFormat showFormat)
	{
		fShowID = showID;
		fShowFormat = showFormat;
	}

	public static CheckShowAvailRqst newInstance(ProviderShowID showID, ShowFormat showFormat)
	{
		return new CheckShowAvailRqst(showID, showFormat);
	}

	/* Implementation */
	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ShowID", fShowID, ShowID.MaxLength);
		writer.writeObject("ShowFormat", fShowFormat);
	}
}
