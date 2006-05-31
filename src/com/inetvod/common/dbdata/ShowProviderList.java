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
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.data.ProviderIDList;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ShowProviderID;
import com.inetvod.common.data.ProviderConnectionID;
import com.inetvod.common.data.ShowAvail;

public class ShowProviderList extends ArrayList<ShowProvider>
{
	/* Construction */
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

	public static void markUnavailByProviderConnectionID(ProviderConnectionID providerConnectionID) throws Exception
	{
		DatabaseProcParam params[] = new DatabaseProcParam[1];

		params[0] = new DatabaseProcParam(Types.VARCHAR, providerConnectionID.toString());

		ShowProvider.getDatabaseAdaptor().executeProc("ShowProvider_MarkUnavailByProviderConnectionID", params);
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

	/**
	 * Returns a sub-set of items from this list that have the specified ShowID
	 * @param showID
	 * @return sub-set of ShowProviderList
	 */
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

	/**
	 * Returns a sub-set of items from this list that have the specified ProviderID
	 * @param providerIDList
	 * @return sub-set of ShowProviderList
	 */
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

	/**
	 * Returns a sub-set of items from this list whose ShowAvail is Available
	 * @return sub-set of ShowProviderList
	 */
	public ShowProviderList findItemsByAvailable()
	{
		ShowProviderList showProviderList = new ShowProviderList();

		for(ShowProvider showProvider : this)
		{
			if(ShowAvail.Available.equals(showProvider.getShowAvail()))
				showProviderList.add(showProvider);
		}

		return showProviderList;
	}

	/**
	 * Returns a sub-set of items from this list whose ShowFormatMime is in mimeTypeList
	 * @param mimeTypeList
	 * @return sub-set of ShowProviderList
	 */
	public ShowProviderList findItemsByShowFormatMimeList(String[] mimeTypeList)
	{
		ShowProviderList showProviderList = new ShowProviderList();

		for(ShowProvider showProvider : this)
		{
			for(String mimeType : mimeTypeList)
			{
				if(mimeType.equals(showProvider.getShowFormatMime()))
					showProviderList.add(showProvider);
			}
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
