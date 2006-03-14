/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.sql.Types;
import java.util.ArrayList;

import com.inetvod.common.data.MemberID;

public class RentedShowList extends ArrayList<RentedShow>
{
	/* Construction Methods */
	public RentedShowList()
	{
	}

	public static RentedShowList findByMemberID(MemberID memberID) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, memberID.toString());

		return RentedShow.getDatabaseAdaptor().selectManyByProc("RentedShow_GetByMemberID", params);
	}
}
