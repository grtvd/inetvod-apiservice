/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.util.ArrayList;

public class RatingList extends ArrayList<Rating>
{
	/* Constuction Methods */
	public static RatingList find() throws Exception
	{
		return Rating.getDatabaseAdaptor().selectManyByProc("Rating_GetAll", null);
	}
}
