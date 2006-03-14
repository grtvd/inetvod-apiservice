/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.util.ArrayList;

public class RatingList extends ArrayList<Rating>
{
	/* Construction */
	public static RatingList find() throws Exception
	{
		return Rating.getDatabaseAdaptor().selectManyByProc("Rating_GetAll", null);
	}
}
