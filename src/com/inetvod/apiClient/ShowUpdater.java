/**
 * Copyright © 2006 iNetVOD, Inc. All Rights Reserved.
 * iNetVOD Confidential and Proprietary.  See LEGAL.txt.
 */
package com.inetvod.apiClient;

import com.inetvod.common.core.Logger;
import com.inetvod.common.data.CategoryID;
import com.inetvod.common.data.CategoryIDList;
import com.inetvod.common.data.ProviderID;
import com.inetvod.common.data.ShowAvail;
import com.inetvod.common.data.ShowFormat;
import com.inetvod.common.data.ShowID;
import com.inetvod.common.data.ShowRental;
import com.inetvod.common.dbdata.Provider;
import com.inetvod.common.dbdata.ProviderConnection;
import com.inetvod.common.dbdata.Show;
import com.inetvod.common.dbdata.ShowCategory;
import com.inetvod.common.dbdata.ShowCategoryList;
import com.inetvod.common.dbdata.ShowList;
import com.inetvod.common.dbdata.ShowProvider;

public abstract class ShowUpdater
{
	//TODO: see what methods can be combined from ProviderShowUpdater and ConnectionShowUpdater and added to this class

	/* Fields */
	protected Provider fProvider;
	protected ProviderConnection fProviderConnection;

	/* Construction */
	protected ShowUpdater(Provider provider, ProviderConnection providerConnection)
	{
		fProvider = provider;
		fProviderConnection = providerConnection;
	}

	/* Implementation */
	public abstract void doUpdate() throws Exception;

	protected Show locateExistingShow(ShowData showData) throws Exception
	{
		if((showData.getReleasedYear() == null) && (showData.getReleasedOn() == null))
			return null;

		ShowList showList;

		if(showData.getReleasedOn() != null)
			showList = ShowList.findByNameReleasedOn(showData.getName(), showData.getEpisodeName(),
				showData.getReleasedOn());
		else
			showList = ShowList.findByNameReleasedYear(showData.getName(), showData.getEpisodeName(),
				showData.getReleasedYear());

		//TODO: how do we handle more than one result?
		if(showList.size() >= 1)
			return showList.get(0);

		return null;
	}

	protected void updateShow(ShowData showData) throws Exception
	{
		final String METHOD_NAME = "updateShow";
		ProviderID providerID = fProviderConnection.getProviderID();
		Show show;
		ShowProvider showProvider;

		showProvider = ShowProvider.findByProviderIDProviderShowID(providerID, showData.getProviderShowID());
		if(showProvider != null)
		{
			show = Show.get(showProvider.getShowID());
		}
		else
		{
			show = locateExistingShow(showData);
			if(show != null)
			{
				showProvider = ShowProvider.findByShowIDProviderID(show.getShowID(), providerID);
				if(showProvider != null)
				{
					Logger.logWarn(this, METHOD_NAME, String.format("Provider already set for Show, possible bad ProviderShowID(%s) for ShowID(%s)",
						showData.getProviderShowID(), show.getShowID()));
					return;
				}
			}
			else
				show = Show.newInstance(showData.getName(), showData.getIsAdult());

			showProvider = ShowProvider.newInstance(show.getShowID(), providerID,
				fProviderConnection.getProviderConnectionID(), showData.getProviderShowID());
		}

		saveShowData(showData, show, showProvider);
	}

	private void saveShowData(ShowData showData, Show show, ShowProvider showProvider) throws Exception
	{
		//TODO: need to have logic for updating same Show from two Providers

		// update Show values
		show.setName(showData.getName());
		show.setEpisodeName(showData.getEpisodeName());
		show.setEpisodeNumber(showData.getEpisodeNumber());
		show.setReleasedOn(showData.getReleasedOn());
		show.setReleasedYear(showData.getReleasedYear());
		show.setDescription(showData.getDescription());
		show.setRunningMins(showData.getRunningMins());
		show.setPictureURL(showData.getPictureURL());
		show.setRatingID(showData.getRatingID());
		//TODO: showData.getLanguageID();
		show.setIsAdult(showData.getIsAdult());
		show.update();

		// updte ShowProvider values
		//TODO: need to support multiple ShowCost/ShowFormat records
		ShowRental showRental = showData.getShowRentalList().get(0);
		ShowFormat showFormat = showRental.getShowFormatList().get(0);
		if(showFormat instanceof ShowFormatExt)
		{
			showProvider.setShowURL(((ShowFormatExt)showFormat).getShowURL());
			showProvider.setShowFormatMime(((ShowFormatExt)showFormat).getShowFormatMime());
		}
		//TODO: need to support multiple ShowCost/ShowFormat records
		showProvider.setShowCost(showRental.getShowCostList().get(0));
		showProvider.setShowAvail(ShowAvail.Available);
		showProvider.update();

		// update ShowCategory entries
		reconcileShowCategory(showData, show.getShowID());
	}

	private void reconcileShowCategory(ShowData showData, ShowID showID) throws Exception
	{
		ShowCategoryList showCategoryList = ShowCategoryList.findByShowID(showID);

		CategoryIDList providerCategoryIDList = showData.getCategoryIDList();

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
