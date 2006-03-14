/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.util.ArrayList;

import com.inetvod.common.data.CategoryIDList;

public class CategoryList extends ArrayList<Category>
{
	/* Construction */
	public static CategoryList find() throws Exception
	{
		return Category.getDatabaseAdaptor().selectManyByProc("Category_GetAll", null);
	}

	/* Implementation */
	public CategoryIDList getIDList()
	{
		CategoryIDList categoryIDList = new CategoryIDList();

		for(Category category : this)
			categoryIDList.add(category.getCategoryID());

		return categoryIDList;
	}
}
