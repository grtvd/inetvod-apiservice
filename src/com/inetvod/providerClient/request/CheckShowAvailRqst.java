/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.data.ProviderShowID;
import com.inetvod.common.data.ShowCost;
import com.inetvod.common.data.ShowFormat;

public class CheckShowAvailRqst implements Writeable
{
	/* Fields */
	private ProviderShowID fShowID;
	private ShowFormat fShowFormat;
	private ShowCost fShowCost;

	/* Construction */
	private CheckShowAvailRqst(ProviderShowID showID, ShowFormat showFormat, ShowCost showCost)
	{
		fShowID = showID;
		fShowFormat = showFormat;
		fShowCost = showCost;
	}

	public static CheckShowAvailRqst newInstance(ProviderShowID showID, ShowFormat showFormat, ShowCost showCost)
	{
		return new CheckShowAvailRqst(showID, showFormat, showCost);
	}

	/* Implementation */
	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ShowID", fShowID, ProviderShowID.MaxLength);
		writer.writeObject("ShowFormat", fShowFormat);
		writer.writeObject("ShowCost", fShowCost);
	}
}
