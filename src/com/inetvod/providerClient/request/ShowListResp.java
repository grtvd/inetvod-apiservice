/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataReader;
import com.inetvod.common.core.Readable;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ShowIDList;

public class ShowListResp implements Readable
{
	/* Fields */
	private ShowIDList fShowIDList;

	/* Getters and Setters */
	public ShowIDList getShowIDList() { return fShowIDList; }

	/* Construction */
	public ShowListResp(DataReader reader) throws Exception
	{
		readFrom(reader);
	}

	/* Implementation */
	public void readFrom(DataReader reader) throws Exception
	{
		fShowIDList = reader.readStringList("ShowID", ShowID.MaxLength, ShowIDList.Ctor, ShowID.CtorString);
	}
}
