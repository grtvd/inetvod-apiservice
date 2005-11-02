/**
 * Copyright © 2005 iNetVOD, Inc. All Rights Reserved.
 * Confidential and Proprietary
 */
package com.inetvod.apiservice;

import java.util.ArrayList;

import com.inetvod.common.core.Logger;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ShowIDList;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.Show;
import com.inetvod.common.dbdata.ShowCategory;
import com.inetvod.common.dbdata.ShowCategoryList;
import com.inetvod.common.dbdata.ShowList;
import com.inetvod.common.dbdata.ShowProvider;
import com.inetvod.providerClient.ProviderRequestor;
import com.inetvod.providerClient.rqdata.ShowDetail;
import com.inetvod.providerClient.rqdata.ShowDetailList;

public class ProviderShowUpdater
{
	/* Constants */
	private static final int UpdateGroupSize = 25;	//TODO: add as configuration item, possibly by Provider

	/* Fields */
	private static ProviderShowUpdater fProviderShowUpdater = new ProviderShowUpdater();

	/* Getters and Setters */
	public static ProviderShowUpdater getThe() { return fProviderShowUpdater; }

	/* Construction */
	private ProviderShowUpdater()
	{
	}

	/* Implementation */
	public void doUpdate(ProviderID providerID) throws Exception
	{
		final String METHOD_NAME = "doUpdate";
		Provider provider = Provider.get(providerID);
		ProviderRequestor providerRequestor = ProviderRequestor.newInstance(provider);

		//TODO: set all ShowProvider Status of Available to Confirming

		ShowIDList completeShowIDList = providerRequestor.showList();
		ArrayList<ShowIDList> updateList = makeUpdateList(completeShowIDList);

		ShowDetailList showDetailList;
		Show show;
		ShowProvider showProvider;

		for(ShowIDList showIDList : updateList)
		{
			showDetailList = providerRequestor.showDetail(showIDList);
			for(ShowDetail showDetail : showDetailList)
			{
				if(!confirmShowData(showDetail))
					continue;

				showProvider = ShowProvider.findByProviderIDProviderShowID(providerID, showDetail.getProviderShowID());
				if(showProvider != null)
				{
					show = Show.get(showProvider.getShowID());
				}
				else
				{
					show = locateExistingShow(showDetail);
					if(show != null)
					{
						showProvider = ShowProvider.findByShowIDProviderID(show.getShowID(), providerID);
						if(showProvider != null)
						{
							Logger.logWarn(this, METHOD_NAME, String.format("Provider already set for Show, possible bad ProviderShowID(%s) for ShowID(%s)",
								showDetail.getProviderShowID(), show.getShowID()));
							continue;
						}
					}
					else
						show = Show.newInstance(showDetail.getName(), showDetail.getIsAdult());

					showProvider = ShowProvider.newInstance(show.getShowID(), providerID, showDetail.getProviderShowID());
				}

				updateShowData(showDetail, show, showProvider);
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

	private Show locateExistingShow(ShowDetail showDetail) throws Exception
	{
		if(showDetail.getReleasedYear() == null)
			return null;

		ShowList showList = ShowList.findByNameReleasedYear(showDetail.getName(), showDetail.getEpisodeName(),
			showDetail.getReleasedYear());

		//TODO: how do we handle more than one result?
		if(showList.size() >= 1)
			return showList.get(0);

		return null;
	}

	private void updateShowData(ShowDetail showDetail, Show show, ShowProvider showProvider) throws Exception
	{
		//TODO: need to have logic for updating same Show from two Providers

		// update Show values
		show.setName(showDetail.getName());
		show.setEpisodeName(showDetail.getEpisodeName());
		show.setEpisodeNumber(showDetail.getEpisodeNumber());
		show.setReleasedOn(showDetail.getReleasedOn());
		show.setReleasedYear(showDetail.getReleasedYear());
		show.setDescription(showDetail.getDescription());
		show.setRunningMins(showDetail.getRunningMins());
		show.setPictureURL(showDetail.getPictureURL());
		show.setRatingID(showDetail.getRatingID());
		show.setIsAdult(showDetail.getIsAdult());
		show.update();

		// updte ShowProvider values
		//TODO: need to support multiple ShowCost/ShowFormat records
		showProvider.setShowCost(showDetail.getShowRentalList().get(0).getShowCostList().get(0));
		showProvider.update();

		// update ShowCategory entries
		reconcileShowCategory(showDetail, show.getShowID());
	}

	private void reconcileShowCategory(ShowDetail showDetail, ShowID showID) throws Exception
	{
		ShowCategoryList showCategoryList = ShowCategoryList.findByShowID(showID);

		//TODO: map Provider's Categories to iNetVOD Categories
		CategoryIDList providerCategoryIDList = showDetail.getCategoryIDList();

		// remove old categories
		for(ShowCategory showCategory : showCategoryList)
		{
			if(CategoryID.Featured.equals(showCategory.getCategoryID()))	// skip Featured
				continue;

			if(!providerCategoryIDList.contains(showCategory.getCategoryID()))
				showCategory.delete();
		}

		// add new categories
		CategoryIDList categoryIDList = showCategoryList.getCategoryIDList();
		for(CategoryID categoryID : providerCategoryIDList)
		{
			if(CategoryID.Featured.equals(categoryID))	// skip Featured
				continue;

			if(!categoryIDList.contains(categoryID))
				ShowCategory.newInstance(showID, categoryID).update();
		}
	}
}
