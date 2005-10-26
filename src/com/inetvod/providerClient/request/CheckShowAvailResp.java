/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.dbdata.ShowCost;
import com.inetvod.common.dbdata.ShowCostList;

public class CheckShowAvailResp implements Readable
{
	/* Fields */
	private ShowCostList fShowCostList;

	/* Getters and Setters */
	public ShowCostList getShowCostList() { return fShowCostList; }

	/* Construction */
	public CheckShowAvailResp(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowCostList = reader.readList("ShowCost", ShowCostList.Ctor, ShowCost.CtorDataReader);
	}
}
