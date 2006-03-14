/**
 * Copyright © 2004-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.common.dbdata;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;

import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.MemberID;
import com.inetvod.common.data.ShowCategoryID;
import com.inetvod.common.data.ShowID;

public class ShowCategoryList extends ArrayList<ShowCategory>
{
	/* Construction */
	public ShowCategoryList()
	{
	}

	public static ShowCategoryList findByShowName(String partialName) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, partialName);

		return ShowCategory.getDatabaseAdaptor().selectManyByProc("ShowCategory_Search", params);
	}

	public static ShowCategoryList findByShowID(ShowID showID) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, showID.toString());

		return ShowCategory.getDatabaseAdaptor().selectManyByProc("ShowCategory_GetByShowID", params);
	}

	public static ShowCategoryList findByCategoryIDList(CategoryIDList categoryIDList) throws Exception
	{
		ShowCategoryList showCategoryList = new ShowCategoryList();
		Iterator iter = categoryIDList.iterator();
		CategoryID categoryID;
		DatabaseProcParam params[];

		while(iter.hasNext())
		{
			categoryID = (CategoryID)iter.next();
			params = new DatabaseProcParam[1];

			params[0] = new DatabaseProcParam(Types.VARCHAR, categoryID.toString());

			showCategoryList.merge(ShowCategory.getDatabaseAdaptor().selectManyByProc("ShowCategory_GetByCategoryID", params));
		}

		return showCategoryList;
	}

	public static ShowCategoryList findByRentedShowMemberID(MemberID memberID) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, memberID.toString());

		return ShowCategory.getDatabaseAdaptor().selectManyByProc("ShowCategory_GetByRentedShowMemberID", params);
	}

	/* Group Methods */

	/**
	 * Returns a sub-set of items from this list that have the specified ShowID
	 * @param showID
	 * @return ShowCategoryList
	 */
	public ShowCategoryList findItemsByShowID(ShowID showID)
	{
		ShowCategoryList showCategoryList = new ShowCategoryList();
		ShowCategory showCategory;
		Iterator iter = iterator();

		while(iter.hasNext())
		{
			showCategory = (ShowCategory)iter.next();
			if(showCategory.getShowID().equals(showID))
				showCategoryList.add(showCategory);
		}

		return showCategoryList;
	}

	/**
	 * Returns a sub-set of items from this list that have the specified CategoryID
	 * @param categoryIDList
	 * @return ShowCategoryList
	 */
	public ShowCategoryList findItemsByCategoryIDList(CategoryIDList categoryIDList)
	{
		ShowCategoryList showCategoryList = new ShowCategoryList();
		ShowCategory showCategory;
		Iterator iter = iterator();

		while(iter.hasNext())
		{
			showCategory = (ShowCategory)iter.next();
			if(categoryIDList.contains(showCategory.getCategoryID()))
				showCategoryList.add(showCategory);
		}

		return showCategoryList;
	}

	/**
	 * Returns a list of CategoryID from the items in this list.
	 * @return ShowCategoryList
	 */
	public CategoryIDList getCategoryIDList()
	{
		CategoryIDList categoryIDList = new CategoryIDList();
		ShowCategory showCategory;
		Iterator iter = iterator();

		while(iter.hasNext())
		{
			showCategory = (ShowCategory)iter.next();
			categoryIDList.add(showCategory.getCategoryID());
		}

		return categoryIDList;
	}

	/* Item Methods */
	public ShowCategory getItem(int index)
	{
		return get(index);
	}

	@SuppressWarnings({"MethodOverloadsMethodOfSuperclass"})
	public int indexOf(ShowCategoryID showCategoryID)
	{
		for(int i = 0; i < size(); i++)
			if(getItem(i).getShowCategoryID().equals(showCategoryID))
				return i;

		return -1;
	}

	@SuppressWarnings({"MethodOverloadsMethodOfSuperclass"})
	public ShowCategory get(ShowCategoryID showCategoryID) throws Exception
	{
		int pos = indexOf(showCategoryID);
		if(pos >= 0)
			return getItem(pos);

		throw new Exception("ShowCategory not found");
	}

	public ShowCategory find(ShowCategoryID showCategoryID)
	{
		int pos = indexOf(showCategoryID);
		if(pos >= 0)
			return getItem(pos);

		return null;
	}

	/* Group Methods */
	public void merge(ShowCategoryList showCategoryList)
	{
		Iterator iter = showCategoryList.iterator();
		ShowCategory showCategory;

		while(iter.hasNext())
		{
			showCategory = (ShowCategory)iter.next();
			if(find(showCategory.getShowCategoryID()) == null)
				add(showCategory);
		}
	}
}
