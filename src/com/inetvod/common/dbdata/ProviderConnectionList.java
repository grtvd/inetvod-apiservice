/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.sql.Types;
import java.util.ArrayList;

import com.inetvod.common.data.ProviderID;

public class ProviderConnectionList extends ArrayList<ProviderConnection>
{
	/* Construction */
	public static ProviderConnectionList findByProviderID(ProviderID providerID) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, providerID.toString());

		return ProviderConnection.getDatabaseAdaptor().selectManyByProc("ProviderConnection_GetByProviderID", params);
	}
}
