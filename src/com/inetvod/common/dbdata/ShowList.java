/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.util.ArrayList;
import java.util.Iterator;
import java.sql.Types;

public class ShowList extends ArrayList<Show>
{
	/* Construction Methods */
	public ShowList()
	{
	}

	public static ShowList findByName(String partialName) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, partialName);

		return Show.getDatabaseAdaptor().selectManyByProc("Show_Search", params);
	}

	public static ShowList findByRentedShowMemberID(MemberID memberID) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, memberID.toString());

		return Show.getDatabaseAdaptor().selectManyByProc("Show_GetByRentedShowMemberID", params);
	}

	public static ShowList findByProviderIDList(ProviderIDList providerIDList) throws Exception
	{
		ShowList showList = new ShowList();
		Iterator iter = providerIDList.iterator();
		ProviderID providerID;
		DatabaseProcParam params[];

		while(iter.hasNext())
		{
			providerID = (ProviderID)iter.next();
			params = new DatabaseProcParam[1];

			params[0] = new DatabaseProcParam(Types.VARCHAR, providerID.toString());

			showList.merge(Show.getDatabaseAdaptor().selectManyByProc("Show_GetByProviderID", params));
		}

		return showList;
	}

	public static ShowList findByCategoryIDList(CategoryIDList categoryIDList) throws Exception
	{
		ShowList showList = new ShowList();
		Iterator iter = categoryIDList.iterator();
		CategoryID categoryID;
		DatabaseProcParam params[];

		while(iter.hasNext())
		{
			categoryID = (CategoryID)iter.next();
			params = new DatabaseProcParam[1];

			params[0] = new DatabaseProcParam(Types.VARCHAR, categoryID.toString());

			showList.merge(Show.getDatabaseAdaptor().selectManyByProc("Show_GetByCategoryID", params));
		}

		return showList;
	}

	/* Item Methods */
	public Show getItem(int index)
	{
		return get(index);
	}

	@SuppressWarnings({"MethodOverloadsMethodOfSuperclass"})
	public int indexOf(ShowID showID)
	{
		for(int i = 0; i < size(); i++)
			if(getItem(i).getShowID().equals(showID))
				return i;

		return -1;
	}

	@SuppressWarnings({"MethodOverloadsMethodOfSuperclass"})
	public Show get(ShowID showID) throws Exception
	{
		int pos = indexOf(showID);
		if(pos >= 0)
			return getItem(pos);

		throw new Exception("Show not found");
	}

	public Show find(ShowID showID)
	{
		int pos = indexOf(showID);
		if(pos >= 0)
			return getItem(pos);

		return null;
	}

	/* Group Methods */
	public void merge(ShowList showList)
	{
		Iterator iter = showList.iterator();
		Show show;

		while(iter.hasNext())
		{
			show = (Show)iter.next();
			if(find(show.getShowID()) == null)
				add(show);
		}
	}
}
