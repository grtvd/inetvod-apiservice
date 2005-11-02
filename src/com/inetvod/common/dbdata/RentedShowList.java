/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
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
