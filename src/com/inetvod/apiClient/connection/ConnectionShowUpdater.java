/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.inetvod.apiClient.ShowData;
import com.inetvod.apiClient.ShowDataList;
import com.inetvod.apiClient.ShowUpdater;
import com.inetvod.common.core.LanguageID;
import com.inetvod.common.core.Logger;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.ProviderConnectionType;
import com.inetvod.common.dbdata.CategoryList;
import com.inetvod.common.dbdata.ProviderConnection;

public class ConnectionShowUpdater extends ShowUpdater
{
	/* Construction */
	private ConnectionShowUpdater(ProviderConnection providerConnection)
	{
		super(providerConnection);
	}

	public static ConnectionShowUpdater newInstance(ProviderConnection providerConnection)
	{
		return new ConnectionShowUpdater(providerConnection);
	}

	/* Implementation */
	public void doUpdate() throws Exception
	{
		BaseConnection connection = createConnection();
		ShowDataList showDataList = connection.process();

		for(ShowData showData : showDataList)
		{
			if(!confirmShowData(showData))
				continue;

			updateShow(showData);
		}
	}

	@SuppressWarnings({"unchecked"})
	private BaseConnection createConnection() throws ClassNotFoundException, NoSuchMethodException,
		IllegalAccessException, InvocationTargetException
	{
		ProviderConnectionType providerConnectionType = fProviderConnection.getProviderConnectionType();
		String connectionName = getClass().getPackage().getName() + "."
			+ providerConnectionType.toString().toLowerCase() + "." + providerConnectionType.toString()
			+ "Connection";

		Class<BaseConnection> cl = (Class<BaseConnection>)Class.forName(connectionName);
		Method method = cl.getMethod("newInstance", ProviderConnection.class);
		return (BaseConnection)method.invoke(cl, fProviderConnection);
	}

	private boolean confirmShowData(ShowData showData /*TODO, RatingIDList validRatingIDList*/) throws Exception
	{
		final String METHOD_NAME = "confirmShowData";
		String providerShowIDStr;
		CategoryIDList allCategoryIDList;
		CategoryIDList categoryIDList;
		boolean valid = true;

		//TODO: validate data returned from Provider
		if(showData.getProviderShowID() == null)
		{
			valid = false;
			Logger.logWarn(this, METHOD_NAME, "Missing ProviderShowID");
			providerShowIDStr = "MISSING";
		}
		else
			providerShowIDStr = showData.getProviderShowID().toString();

		if(showData.getCategoryIDList().size() > 0)
		{
			allCategoryIDList = CategoryList.find().getIDList();
			categoryIDList = new CategoryIDList();

			//TODO: validate CategoryIDs
			for(CategoryID categoryID : showData.getCategoryIDList())
			{
				//TODO: map the CategoryID
				//TODO: confirm "featured" is not returned from Provider
				if(CategoryID.Featured.equals(categoryID) || !allCategoryIDList.contains(categoryID))
					Logger.logWarn(this, METHOD_NAME, String.format("Invalid CategoryID(%s) provided, ProviderShowID(%s)", categoryID, providerShowIDStr));
				else
					categoryIDList.add(categoryID);
			}

			showData.getCategoryIDList().copy(categoryIDList);
		}
		else
		{
			Logger.logInfo(this, METHOD_NAME, String.format("No CategoryIDs provided, ProviderShowID(%s)", providerShowIDStr));
		}

		if(showData.getRatingID() != null)
		{
			//TODO: confirm RatingID;
		}

		if(showData.getLanguageID() != null)
		{
			//TODO: confirm LanguageID
		}
		else
		{
			//valid = false;
			showData.setLanguageID(LanguageID.English);
			Logger.logWarn(this, METHOD_NAME, String.format("LanguageID not provided, ProviderShowID(%s)", providerShowIDStr));
		}

		if(showData.getIsAdult() == null)
		{
			//valid = false;
			showData.setIsAdult(false);
			Logger.logWarn(this, METHOD_NAME, String.format("IsAdult not provided, ProviderShowID(%s)", providerShowIDStr));
		}

		if(showData.getShowRentalList().size() > 0)
		{
			//TODO: confirm ShowFormats are not duplicated
		}
		else
		{
			valid = false;
			Logger.logWarn(this, METHOD_NAME, String.format("No ShowRentals provided, ProviderShowID(%s)", providerShowIDStr));
		}

		return valid;
	}
}
