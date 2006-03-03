/**
 * Copyright � 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.util.ArrayList;

public class CategoryList extends ArrayList<Category>
{
	/* Construction */
	public static CategoryList find() throws Exception
	{
		return Category.getDatabaseAdaptor().selectManyByProc("Category_GetAll", null);
	}
}
