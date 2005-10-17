/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.sql.Types;
import java.util.ArrayList;

public class MemberProviderList extends ArrayList<MemberProvider>
{
	/* Constuction Methods */
	public static MemberProviderList findByMemberID(MemberID memberID) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, memberID.toString());

		return MemberProvider.getDatabaseAdaptor().selectManyByProc("MemberProvider_GetByMemberID", params);
	}
}
