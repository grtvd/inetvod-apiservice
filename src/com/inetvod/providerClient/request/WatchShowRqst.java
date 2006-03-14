/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.data.ProviderShowID;

public class WatchShowRqst implements Writeable
{
	/* Constants */
	private static final int PlayerIPAddressMaxLength = 16;

	/* Fields */
	private ProviderShowID fShowID;
	private String fPlayerIPAddress;

	/* Construction */
	private WatchShowRqst(ProviderShowID showID, String playerIPAddress)
	{
		fShowID = showID;
		fPlayerIPAddress = playerIPAddress;
	}

	public static WatchShowRqst newInstance(ProviderShowID showID, String playerIPAddress)
	{
		return new WatchShowRqst(showID, playerIPAddress);
	}

	/* Implementation */
	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ShowID", fShowID, ProviderShowID.MaxLength);
		writer.writeString("PlayerIPAddress", fPlayerIPAddress, PlayerIPAddressMaxLength);
	}
}
