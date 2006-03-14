/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.providerClient.rqdata.ShowDetail;
import com.inetvod.providerClient.rqdata.ShowDetailList;

public class ShowDetailResp implements Readable
{
	/* Fields */
	private ShowDetailList fShowDetailList;

	/* Getters and Setters */
	public ShowDetailList getShowDetailList() { return fShowDetailList; }

	/* Construction */
	public ShowDetailResp(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowDetailList = reader.readList("ShowDetail", ShowDetailList.Ctor, ShowDetail.CtorDataReader);
	}
}
