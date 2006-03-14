/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.data.ShowCost;

public class CheckShowAvailResp implements Readable
{
	/* Fields */
	private ShowCost fShowCost;

	/* Getters and Setters */
	public ShowCost getShowCost() { return fShowCost; }

	/* Construction */
	public CheckShowAvailResp(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowCost = reader.readObject("ShowCost", ShowCost.CtorDataReader);
	}
}
