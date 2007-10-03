/**
 * Copyright © 2006-2007 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.connection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;

import com.inetvod.apiClient.ShowData;
import com.inetvod.apiClient.ShowDataList;
import com.inetvod.apiClient.ShowFormatExt;
import com.inetvod.apiClient.ShowUpdater;
import com.inetvod.common.core.LanguageID;
import com.inetvod.common.core.Logger;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.ProviderConnectionType;
import com.inetvod.common.data.ShowAvail;
import com.inetvod.common.data.ShowFormat;
import com.inetvod.common.data.ShowRental;
import com.inetvod.common.dbdata.CategoryList;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import com.inetvod.common.dbdata.Show;
import com.inetvod.common.dbdata.ShowProvider;
import com.inetvod.common.dbdata.ShowProviderList;
import com.inetvod.contmgr.data.Info;
import com.inetvod.contmgr.data.MediaMapper;
import com.inetvod.contmgr.mgr.ContentManager;

public class ConnectionShowUpdater extends ShowUpdater
{
	/* Construction */
	private ConnectionShowUpdater(Provider provider, ProviderConnection providerConnection)
	{
		super(provider, providerConnection);
	}

	public static ConnectionShowUpdater newInstance(Provider provider, ProviderConnection providerConnection)
	{
		return new ConnectionShowUpdater(provider, providerConnection);
	}

	/* Implementation */
	public void doUpdate() throws Exception
	{
		if(!fProviderConnection.isEnabled())
		{
			ShowProviderList.markUnavailByProviderConnectionID(fProviderConnection.getProviderConnectionID());
			return;
		}

		BaseConnection connection = createConnection();
		ShowDataList showDataList = connection.process();

		// Note: fetch the new ShowDateList before marking Recomfirming.  If connection is down, will leave
		// current data unchanged.

		ShowProviderList.markReconfirmByProviderConnectionIDAvail(fProviderConnection.getProviderConnectionID());

		if(showDataList != null)
		{
			for(ShowData showData : showDataList)
			{
				if(!confirmShowData(showData))
					continue;

				updateShow(showData);
			}
		}

		processReconfirming();
		processUnconfirmed();
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
		Method method = cl.getMethod("newInstance", Provider.class, ProviderConnection.class);
		return (BaseConnection)method.invoke(cl, fProvider, fProviderConnection);
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
			//TODO Logger.logInfo(this, METHOD_NAME, String.format("No CategoryIDs provided, ProviderShowID(%s)", providerShowIDStr));
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
			//TODO Logger.logWarn(this, METHOD_NAME, String.format("LanguageID not provided, ProviderShowID(%s)", providerShowIDStr));
		}

		if(showData.getIsAdult() == null)
		{
			//valid = false;
			showData.setIsAdult(false);
			//TODO Logger.logWarn(this, METHOD_NAME, String.format("IsAdult not provided, ProviderShowID(%s)", providerShowIDStr));
		}

		if(showData.getShowRentalList().size() > 0)
		{
			/* For now, ShowFormatMime is used as unique identifier for ShowFormat (see ShowUpdater.reconcileShowProvider()).
				Need to confirm not receiving two formats that are the same (if so, probably vary in quality).
				TODO: Later, need to map by show quality, not just mime.
			 */
			HashSet<String> showFormatMimeList = new HashSet<String>();
			for(ShowRental showRental : showData.getShowRentalList())
				for(ShowFormat showFormat : showRental.getShowFormatList())
				{
					if(showFormat instanceof ShowFormatExt)
					{
						String showFormatMime = ((ShowFormatExt)showFormat).getShowFormatMime();
						if(showFormatMimeList.contains(showFormatMime))
						{
							valid = false;
							Logger.logWarn(this, METHOD_NAME, String.format("Duplicate ShowFormatMime(%s), ProviderShowID(%s)",
								showFormatMime, providerShowIDStr));
						}
						showFormatMimeList.add(showFormatMime);
					}
					else
					{
						valid= false;
						Logger.logErr(this, METHOD_NAME, String.format("Expecting ShowFormatExt, ProviderShowID(%s)", providerShowIDStr));
					}
				}
		}
		else
		{
			valid = false;
			Logger.logWarn(this, METHOD_NAME, String.format("No ShowRentals provided, ProviderShowID(%s)", providerShowIDStr));
		}

		return valid;
	}

	private void processReconfirming() throws Exception
	{
		ShowProviderList showProviderList = ShowProviderList.findByProviderConnectionIDReconfirm(
			this.fProviderConnection.getProviderConnectionID());

		for(ShowProvider showProvider : showProviderList)
		{
			if(ContentManager.checkContent(showProvider.getShowURL()))
			{
				showProvider.setShowAvail(ShowAvail.Available);
				showProvider.update();
			}
		}
	}

	private void processUnconfirmed() throws Exception
	{
		ShowProviderList showProviderList = ShowProviderList.findByProviderConnectionIDUnconfirm(
			this.fProviderConnection.getProviderConnectionID());

		for(ShowProvider showProvider : showProviderList)
		{
			Info info = ContentManager.getStats(showProvider.getShowURL(), null);
			if(info != null)
			{
				showProvider.setShowAvail(ShowAvail.Available);
				showProvider.setShowFormat(new ShowFormat(null,
					MediaMapper.getMediaEncodingForVideoAudioCodecs(info.getVideoCodec(), info.getAudioCodec()),
					MediaMapper.getMediaContainerForVideoAudioCodecs(info.getVideoCodec(), info.getAudioCodec()),
					info.getHorzResolution(), info.getVertResolution(), info.getFramesPerSecond(), info.getBitRate()));
				showProvider.update();

				//TODO bulk load ShowList by ProviderConnectionID
				//ShowList showList = ShowList.findByProviderConnectionID(this.fProviderConnection.getProviderConnectionID());
				if(info.getRunningTimeSecs() != null)
				{
					Show show = Show.get(showProvider.getShowID());
					if(show.getRunningMins() == null)
					{
						//noinspection MagicNumber
						show.setRunningMins((short)((info.getRunningTimeSecs().doubleValue() / 60.0) + 0.5));
						show.update();
					}
				}
			}
		}
	}
}
