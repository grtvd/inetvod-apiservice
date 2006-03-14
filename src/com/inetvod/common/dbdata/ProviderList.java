/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
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
