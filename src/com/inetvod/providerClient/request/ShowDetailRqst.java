/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.providerClient.request;

import com.inetvod.common.core.DataWriter;
import com.inetvod.common.core.Writeable;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ShowIDList;

public class ShowDetailRqst implements Writeable
{
	/* Fields */
	private ShowIDList fShowIDList;

	/* Construction */
	private ShowDetailRqst(ShowIDList showIDList)
	{
		fShowIDList = showIDList;
	}

	public static ShowDetailRqst newInstance(ShowIDList showIDList)
	{
		return new ShowDetailRqst(showIDList);
	}

	/* Implementation */
	public void writeTo(DataWriter writer) throws Exception
	{
		writer.writeStringList("ShowID", fShowIDList, ShowID.MaxLength);
	}
}
