/**
 * Copyright © 2005-2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient.providerapi;

import java.util.ArrayList;

import com.inetvod.apiClient.ShowData;
import com.inetvod.apiClient.ShowUpdater;
import com.inetvod.common.core.Logger;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.ShowIDList;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import com.inetvod.common.dbdata.ShowProviderList;
import com.inetvod.providerClient.ProviderRequestor;
import com.inetvod.providerClient.rqdata.ShowDetail;
import com.inetvod.providerClient.rqdata.ShowDetailList;

public class ProviderShowUpdater extends ShowUpdater
{
	/* Constants */
	private static final int UpdateGroupSize = 25;	//TODO: add as configuration item, possibly by Provider

	/* Fields */

	/* Getters and Setters */

	/* Construction */
	private ProviderShowUpdater(Provider provider, ProviderConnection providerConnection)
	{
		super(provider, providerConnection);
	}

	public static ProviderShowUpdater newInstance(Provider provider, ProviderConnection providerConnection)
	{
		return new ProviderShowUpdater(provider, providerConnection);
	}

	/* Implementation */
	public void doUpdate() throws Exception
	{
		if(!fProviderConnection.isEnabled())
		{
			ShowProviderList.markUnavailByProviderConnectionID(fProviderConnection.getProviderConnectionID());
			return;
		}

		ProviderRequestor providerRequestor = ProviderRequestor.newInstance(fProviderConnection);

		ShowIDList completeShowIDList = providerRequestor.showList();
		ArrayList<ShowIDList> updateList = makeUpdateList(completeShowIDList);

		// Note: fetch the new ShowIDList before marking Unavailable.  If connection is down, will leave
		// current data unchanged.

		ShowProviderList.markUnavailByProviderConnectionID(fProviderConnection.getProviderConnectionID());

		ShowDetailList showDetailList;
		ShowData showData;

		for(ShowIDList showIDList : updateList)
		{
			showDetailList = providerRequestor.showDetail(showIDList);
			for(ShowDetail showDetail : showDetailList)
			{
				if(!confirmShowData(showDetail))
					continue;

				showData = convertToShowData(showDetail);

				updateShow(showData);
			}

		}

		//TODO: clean-up unavailable ShowProvider records
		//TODO: clean-up Shows with no ShowProvider records
	}

	private ArrayList<ShowIDList> makeUpdateList(ShowIDList completeShowIDList)
	{
		ArrayList<ShowIDList> showIDLists = new ArrayList<ShowIDList>();
		ShowIDList showIDList;
		int numItems = completeShowIDList.size();
		int numGroups = ((numItems - 1) / UpdateGroupSize) + 1;
		int fromIndex;
		int toIndex;

		for(int i = 0; i < numGroups; i++)
		{
			showIDList = new ShowIDList();
			fromIndex = i * UpdateGroupSize;
			toIndex = fromIndex + ((i == numGroups - 1) ? (numItems % UpdateGroupSize) : UpdateGroupSize);
			showIDList.addAll(completeShowIDList.subList(fromIndex, toIndex));
			showIDLists.add(showIDList);
		}

		return showIDLists;
	}

	private boolean confirmShowData(ShowDetail showDetail /*TODO, RatingIDList validRatingIDList*/)
	{
		final String METHOD_NAME = "confirmShowData";
		String providerShowIDStr;
		boolean valid = true;

		//TODO: validate data returned from Provider
		if(showDetail.getProviderShowID() == null)
		{
			valid = false;
			Logger.logWarn(this, METHOD_NAME, "Missing ProviderShowID");
			providerShowIDStr = "MISSING";
		}
		else
			providerShowIDStr = showDetail.getProviderShowID().toString();

		if(showDetail.getCategoryIDList().size() > 0)
		{
			//TODO: validate CategoryIDs
			//TODO: confirm "featured" is not returned from Provider
			if(showDetail.getCategoryIDList().contains(CategoryID.Featured))
			{
				valid = false;
				Logger.logWarn(this, METHOD_NAME, String.format("Invalid CategoryID provided, ProviderShowID(%s)", providerShowIDStr));
			}
		}
		else
		{
			Logger.logInfo(this, METHOD_NAME, String.format("No CategoryIDs provided, ProviderShowID(%s)", providerShowIDStr));
		}

		if(showDetail.getRatingID() != null)
		{
			//TODO: confirm RatingID;
		}

		if(showDetail.getLanguageID() != null)
		{
			//TODO: confirm LanguageID
		}
		else
		{
			valid = false;
			Logger.logWarn(this, METHOD_NAME, String.format("LanguageID not provided, ProviderShowID(%s)", providerShowIDStr));
		}

		if(showDetail.getIsAdult() == null)
		{
			valid = false;
			Logger.logWarn(this, METHOD_NAME, String.format("IsAdult not provided, ProviderShowID(%s)", providerShowIDStr));
		}

		if(showDetail.getShowRentalList().size() > 0)
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

	private ShowData convertToShowData(ShowDetail showDetail)
	{
		ShowData showData = new ShowData();

		showData.setProviderShowID(showDetail.getProviderShowID());
		showData.setName(showDetail.getName());
		showData.setEpisodeName(showDetail.getEpisodeName());
		showData.setEpisodeNumber(showDetail.getEpisodeNumber());
		showData.setReleasedOn(showDetail.getReleasedOn());
		showData.setReleasedYear(showDetail.getReleasedYear());
		showData.setDescription(showDetail.getDescription());
		showData.setRunningMins(showDetail.getRunningMins());
		showData.setPictureURL(showDetail.getPictureURL());
		//TODO: map Provider's Categories to iNetVOD Categories
		showData.getCategoryIDList().copy(showDetail.getCategoryIDList());
		showData.setRatingID(showDetail.getRatingID());
		showData.setLanguageID(showDetail.getLanguageID());
		showData.setIsAdult(showDetail.getIsAdult());
		showData.getShowRentalList().copy(showDetail.getShowRentalList());

		return showData;
	}
}
