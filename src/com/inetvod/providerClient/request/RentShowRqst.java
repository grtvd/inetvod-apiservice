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
import com.inetvod.common.data.ShowID;
import com.inetvod.providerClient.rqdata.Payment;

public class RentShowRqst implements Writeable
{
	/* Constants */
	private static final int PlayerIPAddressMaxLength = 16;

	/* Fields */
	private ProviderShowID fShowID;
	private String fPlayerIPAddress;
	private ShowFormat fShowFormat;
	private ShowCost fApprovedCost;
	private Payment fPayment;

	/* Construction */
	private RentShowRqst(ProviderShowID showID, String playerIPAddress, ShowFormat showFormat, ShowCost approvedCost,
		Payment payment)
	{
		fShowID = showID;
		fPlayerIPAddress = playerIPAddress;
		fShowFormat = showFormat;
		fApprovedCost = approvedCost;
		fPayment = payment;
	}

	public static RentShowRqst newInstance(ProviderShowID showID, String playerIPAddress, ShowFormat showFormat,
		ShowCost approvedCost, Payment payment)
	{
		return new RentShowRqst(showID, playerIPAddress, showFormat, approvedCost, payment);
	}

	/* Implementation */
	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeDataID("ShowID", fShowID, ProviderShowID.MaxLength);
		writer.writeString("PlayerIPAddress", fPlayerIPAddress, PlayerIPAddressMaxLength);
		writer.writeObject("ShowFormat", fShowFormat);
		writer.writeObject("ApprovedCost", fApprovedCost);
		writer.writeObject("Payment", fPayment);
	}
}
