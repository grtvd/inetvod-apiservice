/**
 * Copyright © 2004-2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.common.dbdata;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Iterator;

import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.data.ProviderIDList;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ShowProviderID;

public class ShowProviderList extends ArrayList<ShowProvider>
{
	/* Constuction Methods */
	public ShowProviderList()
	{
	}

	public static ShowProviderList findByShowID(ShowID showID) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, showID.toString());

		return ShowProvider.getDatabaseAdaptor().selectManyByProc("ShowProvider_GetByShowID", params);
	}

	public static ShowProviderList findByShowName(String partialName) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, partialName);

		return ShowProvider.getDatabaseAdaptor().selectManyByProc("ShowProvider_Search", params);
	}

	public static ShowProviderList findByProviderIDList(ProviderIDList providerIDList) throws Exception
	{
		ShowProviderList showProviderList = new ShowProviderList();
		Iterator iter = providerIDList.iterator();
		ProviderID providerID;
		DatabaseProcParam params[];

		while(iter.hasNext())
		{
			providerID = (ProviderID)iter.next();
			params = new DatabaseProcParam[1];

			params[0] = new DatabaseProcParam(Types.VARCHAR, providerID.toString());

			showProviderList.merge(ShowProvider.getDatabaseAdaptor().selectManyByProc("ShowProvider_GetByProviderID", params));
		}

		return showProviderList;
	}

	public static ShowProviderList findByCategoryIDList(CategoryIDList categoryIDList) throws Exception
	{
		ShowProviderList showProviderList = new ShowProviderList();
		Iterator iter = categoryIDList.iterator();
		CategoryID categoryID;
		DatabaseProcParam params[];

		while(iter.hasNext())
		{
			categoryID = (CategoryID)iter.next();
			params = new DatabaseProcParam[1];

			params[0] = new DatabaseProcParam(Types.VARCHAR, categoryID.toString());

			showProviderList.merge(ShowProvider.getDatabaseAdaptor().selectManyByProc("ShowProvider_GetByCategoryID", params));
		}

		return showProviderList;
	}

	/* Item Methods */
	public ShowProvider getItem(int index)
	{
		return get(index);
	}

	@SuppressWarnings({"MethodOverloadsMethodOfSuperclass"})
	public int indexOf(ShowProviderID showProviderID)
	{
		for(int i = 0; i < size(); i++)
			if(getItem(i).getShowProviderID().equals(showProviderID))
				return i;

		return -1;
	}

	@SuppressWarnings({"MethodOverloadsMethodOfSuperclass"})
	public ShowProvider get(ShowProviderID showProviderID) throws Exception
	{
		int pos = indexOf(showProviderID);
		if(pos >= 0)
			return getItem(pos);

		throw new Exception("ShowProvider not found");
	}

	public ShowProvider find(ShowProviderID showProviderID)
	{
		int pos = indexOf(showProviderID);
		if(pos >= 0)
			return getItem(pos);

		return null;
	}

	/* Group Methods */
	/// <summary>
	/// Returns a sub-set of items from this list that have the specified ShowID
	/// </summary>
	/// <param name="showID"></param>
	/// <returns></returns>
	public ShowProviderList findItemsByShowID(ShowID showID)
	{
		ShowProviderList showProviderList = new ShowProviderList();
		ShowProvider showProvider;
		Iterator iter = iterator();

		while(iter.hasNext())
		{
			showProvider = (ShowProvider)iter.next();
			if(showProvider.getShowID().equals(showID))
				showProviderList.add(showProvider);
		}

		return showProviderList;
	}

	/// <summary>
	/// Returns a sub-set of items from this list that have the specified ProviderID
	/// </summary>
	/// <param name="providerIDList"></param>
	/// <returns></returns>
	public ShowProviderList findItemsByProviderIDList(ProviderIDList providerIDList)
	{
		ShowProviderList showProviderList = new ShowProviderList();
		ShowProvider showProvider;
		Iterator iter = iterator();

		while(iter.hasNext())
		{
			showProvider = (ShowProvider)iter.next();
			if(providerIDList.contains(showProvider.getProviderID()))
				showProviderList.add(showProvider);
		}

		return showProviderList;
	}

	public void merge(ShowProviderList showProviderList)
	{
		Iterator iter = showProviderList.iterator();
		ShowProvider showProvider;

		while(iter.hasNext())
		{
			showProvider = (ShowProvider)iter.next();
			if(find(showProvider.getShowProviderID()) == null)
				add(showProvider);
		}
	}
}
