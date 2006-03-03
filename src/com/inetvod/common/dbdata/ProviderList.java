/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.util.ArrayList;

public class ProviderList extends ArrayList<Provider>
{
	/* Construction */
	public static ProviderList find() throws Exception
	{
		return Provider.getDatabaseAdaptor().selectManyByProc("Provider_GetAll", null);
	}
}
